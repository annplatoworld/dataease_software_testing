package io.dataease.extensions.datasource.provider;

import io.dataease.extensions.datasource.dto.*;
import io.dataease.extensions.datasource.model.SQLMeta;
import io.dataease.extensions.datasource.vo.DatasourceConfiguration;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Minimal Provider implementation for unit testing replaceTablePlaceHolder,
 * replaceCalcFieldPlaceHolder, and rebuildSQL(crossDs=true) without a real DB.
 */
class StubProvider extends Provider {

    @Override
    public List<String> getSchema(DatasourceRequest datasourceRequest) {
        return Collections.emptyList();
    }

    @Override
    public List<DatasetTableDTO> getTables(DatasourceRequest datasourceRequest) {
        return Collections.emptyList();
    }

    @Override
    public ConnectionObj getConnection(DatasourceDTO coreDatasource) throws Exception {
        throw new UnsupportedOperationException("StubProvider does not open connections");
    }

    @Override
    public String checkStatus(DatasourceRequest datasourceRequest) throws Exception {
        throw new UnsupportedOperationException("StubProvider does not check status");
    }

    @Override
    public Map<String, Object> fetchResultField(DatasourceRequest datasourceRequest) {
        throw new UnsupportedOperationException("StubProvider does not fetch results");
    }

    @Override
    public List<TableField> fetchTableField(DatasourceRequest datasourceRequest) {
        throw new UnsupportedOperationException("StubProvider does not fetch table fields");
    }

    @Override
    public void hidePW(DatasourceDTO datasourceDTO) {
        // no-op
    }
}
