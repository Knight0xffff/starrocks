// Copyright 2021-present StarRocks, Inc. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.starrocks.connector.parser.trino;

import org.junit.BeforeClass;
import org.junit.Test;

public class TrinoFunctionTransformTest extends TrinoTestBase {
    @BeforeClass
    public static void beforeClass() throws Exception {
        TrinoTestBase.beforeClass();
    }

    @Test
    public void testAggFnTransform() throws Exception {
        String sql = "select approx_distinct(v1) from t0; ";
        assertPlanContains(sql, "output: approx_count_distinct(1: v1)");

        sql = "select arbitrary(v1) from t0; ";
        assertPlanContains(sql, "output: any_value(1: v1)");

        sql = "select approx_percentile(v1, 0.99) from t0;";
        assertPlanContains(sql, "output: percentile_approx(CAST(1: v1 AS DOUBLE), 0.99)");

        sql = "select stddev(v1) from t0;";
        assertPlanContains(sql, "output: stddev_samp(1: v1)");

        sql = "select stddev_pop(v1) from t0;";
        assertPlanContains(sql, "output: stddev(1: v1)");

        sql = "select variance(v1) from t0;";
        assertPlanContains(sql, "output: var_samp(1: v1)");

        sql = "select var_pop(v1) from t0;";
        assertPlanContains(sql, "output: variance(1: v1)");

        sql = "select count_if(v1) from t0;";
        assertPlanContains(sql, "<slot 4> : if(CAST(1: v1 AS BOOLEAN), 1, NULL)", "output: count(4: case)");
    }
}
