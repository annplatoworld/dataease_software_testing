package io.dataease.extensions.datasource.provider;

import io.dataease.extensions.datasource.dto.DatasourceSchemaDTO;
import io.dataease.extensions.datasource.model.SQLMeta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Tag("ChartPipeline")
@Tag("GroupA")
class ProviderSqlConstructionTest {

    private StubProvider provider;

    @BeforeEach
    void setUp() {
        provider = new StubProvider();
    }

    // --- replaceTablePlaceHolder ---

    @Test
    void replaceTablePlaceHolder_replacesTablePlaceholder_withGivenDialect() {
        String sql = "SELECT * FROM DE_PLACEHOLDER_TABLE_0 WHERE 1=1";
        String tableDialect = "\"my_schema\".\"my_table\"";

        String result = provider.replaceTablePlaceHolder(sql, tableDialect);

        assertThat(result).contains(tableDialect);
        assertThat(result).doesNotContain("DE_PLACEHOLDER_TABLE_0");
        assertThat(result).contains("WHERE 1=1");
    }

    @Test
    void replaceTablePlaceHolder_normalizesNewlines() {
        String sql = "SELECT * FROM\r\nDE_PLACEHOLDER_TABLE_0\nWHERE 1=1";
        String tableDialect = "t";

        String result = provider.replaceTablePlaceHolder(sql, tableDialect);

        assertThat(result).doesNotContain("\r\n").doesNotContain("\n");
        assertThat(result).contains(" ");
    }

    @Test
    void replaceTablePlaceHolder_removesAsymmetricSymmetric() {
        String sql = "SELECT * FROM DE_PLACEHOLDER_TABLE_0 ASYMMETRIC SYMMETRIC";
        String result = provider.replaceTablePlaceHolder(sql, "t");
        assertThat(result).doesNotContain("ASYMMETRIC").doesNotContain("SYMMETRIC");
    }

    // --- replaceCalcFieldPlaceHolder ---

    @Test
    void replaceCalcFieldPlaceHolder_emptySqlMeta_returnsUnchanged() {
        SQLMeta sqlMeta = new SQLMeta();
        String sql = "SELECT key1 FROM t WHERE key2";

        String result = provider.replaceCalcFieldPlaceHolder(sql, sqlMeta);

        assertThat(result).isEqualTo(sql);
    }

    @Test
    void replaceCalcFieldPlaceHolder_xFieldsDialect_replacesPlaceholders() {
        SQLMeta sqlMeta = new SQLMeta();
        sqlMeta.setXFieldsDialect(Map.of("key1", "col_a"));

        String sql = "SELECT key1 FROM t";
        String result = provider.replaceCalcFieldPlaceHolder(sql, sqlMeta);

        assertThat(result).contains("col_a");
        assertThat(result).doesNotContain("key1");
    }

    @Test
    void replaceCalcFieldPlaceHolder_customWheresAndExtWheres_replaced() {
        SQLMeta sqlMeta = new SQLMeta();
        sqlMeta.setCustomWheresDialect(Map.of("custom_where", " AND status = 1"));
        sqlMeta.setExtWheresDialect(Map.of("ext_where", " AND org_id = 2"));

        String sql = "SELECT * FROM t WHERE 1=1 custom_where ext_where";
        String result = provider.replaceCalcFieldPlaceHolder(sql, sqlMeta);

        assertThat(result).contains("status = 1").contains("org_id = 2");
        assertThat(result).doesNotContain("custom_where").doesNotContain("ext_where");
    }

    @Test
    void replaceCalcFieldPlaceHolder_whereTreesDialect_replaced() {
        SQLMeta sqlMeta = new SQLMeta();
        sqlMeta.setWhereTreesDialect(Map.of("row_perm", " ( 1=1 ) "));

        String sql = "SELECT * FROM t WHERE row_perm";
        String result = provider.replaceCalcFieldPlaceHolder(sql, sqlMeta);

        assertThat(result).contains("( 1=1 )");
        assertThat(result).doesNotContain("row_perm");
    }

    @Test
    void replaceCalcFieldPlaceHolder_mergeOrder_allDialectsApplied() {
        SQLMeta sqlMeta = new SQLMeta();
        sqlMeta.setXFieldsDialect(Map.of("x1", "axis_col"));
        sqlMeta.setCustomWheresDialect(Map.of("cw", " AND c=1"));
        sqlMeta.setExtWheresDialect(Map.of("ew", " AND e=2"));
        sqlMeta.setWhereTreesDialect(Map.of("wt", " AND rp=3"));

        String sql = "SELECT x1 FROM t WHERE 1=1 cw ew wt";
        String result = provider.replaceCalcFieldPlaceHolder(sql, sqlMeta);

        assertThat(result).contains("axis_col").contains("c=1").contains("e=2").contains("rp=3");
        assertThat(result).doesNotContain("x1").doesNotContain("cw").doesNotContain("ew").doesNotContain("wt");
    }

    // --- rebuildSQL (crossDs=true) ---

    @Test
    void rebuildSQL_crossDsTrue_returnsSqlUnchanged() {
        String sql = "SELECT * FROM some_table WHERE x=1";
        SQLMeta sqlMeta = new SQLMeta();
        sqlMeta.setTableDialect("other_table");
        Map<Long, DatasourceSchemaDTO> dsMap = Collections.emptyMap();

        String result = provider.rebuildSQL(sql, sqlMeta, true, dsMap);

        assertThat(result).isEqualTo(sql);
    }
}
