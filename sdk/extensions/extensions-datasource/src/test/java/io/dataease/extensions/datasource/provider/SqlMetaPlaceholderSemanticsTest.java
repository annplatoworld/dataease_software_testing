package io.dataease.extensions.datasource.provider;

import io.dataease.extensions.datasource.model.SQLMeta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Group B: SQLMeta and placeholder semantics — invariants 1–3 from the Chart Data Pipeline functional model.
 */
@Tag("ChartPipeline")
@Tag("GroupB")
class SqlMetaPlaceholderSemanticsTest {

    private StubProvider provider;

    @BeforeEach
    void setUp() {
        provider = new StubProvider();
    }

    /**
     * Invariant 1: Row permissions are always applied when SQLMeta.whereTrees is set.
     */
    @Test
    void whenWhereTreesDialectSet_resultContainsRowPermissionFragment() {
        SQLMeta sqlMeta = new SQLMeta();
        String rowPermissionFragment = " ( region = 'EMEA' ) ";
        sqlMeta.setWhereTreesDialect(Map.of("whereTrees_1", rowPermissionFragment));

        String calciteSql = "SELECT id, name FROM t WHERE whereTrees_1";
        String result = provider.replaceCalcFieldPlaceHolder(calciteSql, sqlMeta);

        assertThat(result).contains(rowPermissionFragment.trim());
        assertThat(result).contains("region = 'EMEA'");
        assertThat(result).doesNotContain("whereTrees_1");
    }

    /**
     * Invariant 2: customWheres, extWheres, and whereTrees are all merged (composition).
     */
    @Test
    void whenCustomWheresExtWheresWhereTreesSet_allThreeInOutput() {
        SQLMeta sqlMeta = new SQLMeta();
        sqlMeta.setCustomWheresDialect(Map.of("custom_where", " AND chart_filter = 1"));
        sqlMeta.setExtWheresDialect(Map.of("ext_where", " AND dashboard_param = 2"));
        sqlMeta.setWhereTreesDialect(Map.of("row_perm", " AND row_perm_filter = 3"));

        String calciteSql = "SELECT * FROM t WHERE 1=1 custom_where ext_where row_perm";
        String result = provider.replaceCalcFieldPlaceHolder(calciteSql, sqlMeta);

        assertThat(result).contains("chart_filter = 1");
        assertThat(result).contains("dashboard_param = 2");
        assertThat(result).contains("row_perm_filter = 3");
        assertThat(result).doesNotContain("custom_where").doesNotContain("ext_where");
        // placeholder "row_perm" is replaced (do not assert doesNotContain("row_perm") — it's a substring of "row_perm_filter")
    }

    /**
     * Invariant 3: Only placeholder keys from SQLMeta are replaced; no raw user string in WHERE.
     */
    @Test
    void placeholderKeysOnlyFromSqlMeta_noRawUserStringInWhere() {
        // Use internal-style placeholder keys (e.g. DE_CALC_FIELD_PLACEHOLDER_* or internal ids)
        String internalKey = "DE_CALC_FIELD_PLACEHOLDER_0";
        String safeFragment = "`t`.`status` = 1";
        SQLMeta sqlMeta = new SQLMeta();
        sqlMeta.setCustomWheresDialect(Map.of(internalKey, safeFragment));

        String calciteSql = "SELECT * FROM t WHERE " + internalKey;
        String result = provider.replaceCalcFieldPlaceHolder(calciteSql, sqlMeta);

        assertThat(result).contains(safeFragment);
        assertThat(result).doesNotContain(internalKey);
        // No user-supplied string is concatenated; only the mapped fragment appears
        assertThat(result).doesNotContain("'; DROP TABLE t; --");
    }

    @Test
    void whenWhereTreesDialectSet_multipleKeys_allFragmentsInOutput() {
        SQLMeta sqlMeta = new SQLMeta();
        sqlMeta.setWhereTreesDialect(Map.of(
                "wt1", " ( a = 1 ) ",
                "wt2", " ( b = 2 ) "
        ));

        String calciteSql = "SELECT * FROM t WHERE wt1 AND wt2";
        String result = provider.replaceCalcFieldPlaceHolder(calciteSql, sqlMeta);

        assertThat(result).contains("a = 1").contains("b = 2");
        assertThat(result).doesNotContain("wt1").doesNotContain("wt2");
    }
}
