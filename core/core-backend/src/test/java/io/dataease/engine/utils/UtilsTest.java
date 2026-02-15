package io.dataease.engine.utils;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Tag("EngineUtils")
class UtilsTest {

    @Test
    void transFilterTerm_eq_returnsEquals() {
        assertThat(Utils.transFilterTerm("eq")).isEqualTo(" = ");
    }

    @Test
    void transFilterTerm_notEq_returnsNotEquals() {
        assertThat(Utils.transFilterTerm("not_eq")).isEqualTo(" <> ");
    }

    @Test
    void transFilterTerm_lt_returnsLessThan() {
        assertThat(Utils.transFilterTerm("lt")).isEqualTo(" < ");
    }

    @Test
    void transFilterTerm_le_returnsLessThanOrEqual() {
        assertThat(Utils.transFilterTerm("le")).isEqualTo(" <= ");
    }

    @Test
    void transFilterTerm_gt_returnsGreaterThan() {
        assertThat(Utils.transFilterTerm("gt")).isEqualTo(" > ");
    }

    @Test
    void transFilterTerm_ge_returnsGreaterThanOrEqual() {
        assertThat(Utils.transFilterTerm("ge")).isEqualTo(" >= ");
    }

    @Test
    void transFilterTerm_in_returnsIn() {
        assertThat(Utils.transFilterTerm("in")).isEqualTo(" IN ");
    }

    @Test
    void transFilterTerm_notIn_returnsNotIn() {
        assertThat(Utils.transFilterTerm("not in")).isEqualTo(" NOT IN ");
    }

    @Test
    void transFilterTerm_like_returnsLike() {
        assertThat(Utils.transFilterTerm("like")).isEqualTo(" LIKE ");
    }

    @Test
    void transFilterTerm_notLike_returnsNotLike() {
        assertThat(Utils.transFilterTerm("not like")).isEqualTo(" NOT LIKE ");
    }

    @Test
    void transFilterTerm_nullTerm_returnsIsNull() {
        assertThat(Utils.transFilterTerm("null")).isEqualTo(" IS NULL ");
    }

    @Test
    void transFilterTerm_notNull_returnsIsNotNull() {
        assertThat(Utils.transFilterTerm("not_null")).isEqualTo(" IS NOT NULL ");
    }

    @Test
    void transFilterTerm_empty_returnsEquals() {
        assertThat(Utils.transFilterTerm("empty")).isEqualTo(" = ");
    }

    @Test
    void transFilterTerm_notEmpty_returnsNotEquals() {
        assertThat(Utils.transFilterTerm("not_empty")).isEqualTo(" <> ");
    }

    @Test
    void transFilterTerm_between_returnsBetween() {
        assertThat(Utils.transFilterTerm("between")).isEqualTo(" BETWEEN ");
    }

    @Test
    void transFilterTerm_unknown_returnsEmpty() {
        assertThat(Utils.transFilterTerm("unknown")).isEmpty();
    }

    @Test
    void transFilterTerm_emptyString_returnsEmpty() {
        assertThat(Utils.transFilterTerm("")).isEmpty();
    }

    @Test
    void transFilterTerm_uppercase_returnsEmpty() {
        assertThat(Utils.transFilterTerm("EQ")).isEmpty();
    }

    @Test
    void transFilterTerm_nullArg_throwsNullPointerException() {
        assertThatThrownBy(() -> Utils.transFilterTerm(null))
            .isInstanceOf(NullPointerException.class);
    }
}
