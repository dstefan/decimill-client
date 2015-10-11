package decimill.client.functions;

import decimill.Context;
import decimill.Function;
import decimill.FunctionInvocationException;
import decimill.adapters.statistics.StatsAdapter;
import decimill.adapters.statistics.StatsAdapterException;
import decimill.lang.CastException;
import decimill.lang.DM_Array;
import decimill.lang.DM_Boolean;
import decimill.lang.DM_Double;
import decimill.lang.DM_Map;
import decimill.lang.DM_Object;
import decimill.lang.DM_Sample;
import decimill.lang.DM_String;
import decimill.stats.DecisionAnalysis;
import decimill.stats.Histogram;
import java.util.ArrayList;

/**
 * @author David Stefan
 */
public class FunctionsCollection {

    public static void add(Context ctx) {

        ctx.addFunction("evpi", new Function() {

            public DM_Object call(DM_Array<DM_Array<DM_Double>> nbs) throws FunctionInvocationException {

                double[][] matrix = new double[nbs.size()][];

                for (int i = 0; i < nbs.size(); i++) {
                    try {
                        DM_Object o = nbs.get(i);
                        DM_Sample sample = o.toSample();
                        matrix[i] = new double[sample.size()];
                        matrix[i] = sample.cast(new double[sample.size()]);
                    } catch (CastException e) {
                        throw new FunctionInvocationException(e.getMessage());
                    }
                }

                return new DM_Double(DecisionAnalysis.evpi(matrix));
            }
        });

        ctx.addFunction("evppi", new Function() {

            public DM_Object call(DM_Sample param, DM_Array<DM_Array<DM_Double>> nbs) throws FunctionInvocationException {

                double[] vector;
                double[][] matrix = new double[nbs.size()][];

                try {
                    vector = param.cast(new double[param.size()]);
                    for (int i = 0; i < nbs.size(); i++) {
                        DM_Object o = nbs.get(i);
                        DM_Sample sample = o.toSample();
                        matrix[i] = new double[sample.size()];
                        matrix[i] = sample.cast(new double[sample.size()]);
                    }
                } catch (CastException e) {
                    throw new FunctionInvocationException(e.getMessage());
                }

                return new DM_Double(DecisionAnalysis.evppi(vector, matrix));
            }
        });

        ctx.addFunction("mean", new Function() {

            public DM_Object call(DM_Array<DM_Double> samples) throws FunctionInvocationException {

                Double res = null;

                try {
                    res = StatsAdapter.mean(samples);
                } catch (StatsAdapterException e) {
                    throw new FunctionInvocationException(e.getMessage());
                }

                return new DM_Double(res);
            }
        });

        ctx.addFunction("sum", new Function() {

            public DM_Object call(DM_Array<DM_Object> array) {

                DM_Object res = array.get(0);

                for (int i = 1; i < array.size(); i++) {
                    res = res.plus(array.get(i));
                }

                return res;
            }
        });

        ctx.addFunction("lp", new Function() {

            public DM_Object call(DM_Sample sample) throws FunctionInvocationException {
                
                DM_Sample lps = (DM_Sample) sample.lt(new DM_Double(0.0));
                Double lp;

                try {
                    lp = StatsAdapter.mean(lps);
                } catch (StatsAdapterException e) {
                    throw new FunctionInvocationException(e.getMessage());
                }

                return new DM_Double(lp);
            }
        });

        ctx.addFunction("if", new Function() {

            public DM_Object call(DM_Boolean cond, DM_Object a, DM_Object b) {

                if (cond.getValue()) {
                    return a;
                } else {
                    return b;
                }
            }

            public DM_Object call(DM_Array<DM_Object> cond, DM_Object a, DM_Object b) throws FunctionInvocationException {

                DM_Array<DM_Object> res = new DM_Array<>();
                DM_Array<DM_Object> aArray;
                DM_Array<DM_Object> bArray;

                try {

                    if (!(a instanceof DM_Array)) {
                        aArray = (DM_Array) a.toArray(cond.size());
                    } else {
                        aArray = (DM_Array) a;
                    }

                    if (!(b instanceof DM_Array)) {
                        bArray = (DM_Array) b.toArray(cond.size());
                    } else {
                        bArray = (DM_Array) b;
                    }

                    for (int i = 0; i < cond.size(); i++) {

                        DM_Object o = cond.get(i);

                        if (o instanceof DM_Array) {
                            res.add(call((DM_Array) o, aArray.get(i), bArray.get(i)));
                        } else if (o instanceof DM_Sample) {
                            res.add(call((DM_Sample) o, aArray.get(i).toSample(), bArray.get(i).toSample()));
                        } else {
                            if (((DM_Boolean) o.cast(DM_Boolean.class)).getValue()) {
                                res.add(aArray.get(i));
                            } else {
                                res.add(bArray.get(i));
                            }
                        }
                    }

                } catch (CastException e) {
                    throw new FunctionInvocationException(e.getMessage());
                }

                return res;
            }

            public DM_Object call(DM_Sample cond, DM_Sample a, DM_Sample b) throws FunctionInvocationException {
                
                DM_Sample sample = new DM_Sample();
                ArrayList<DM_Boolean> conds = (ArrayList<DM_Boolean>) cond.cast(DM_Boolean.class);

                for (int i = 0; i < cond.size(); i++) {
                    if (((Boolean) conds.get(i).cast(Boolean.class))) {
                        sample.add(a.get(i));
                    } else {
                        sample.add(b.get(i));
                    }
                }
                return sample;
            }
        });

        ctx.addFunction("hist", new Function() {

            public DM_Map call(DM_String title, DM_Array<DM_Double> samples) throws FunctionInvocationException {

                double[] array = new double[samples.size()];
                
                try {
                    for (int i = 0; i < samples.size(); i++) {
                        array[i] = (double) samples.get(i).cast(Double.class);
                    }
                } catch (CastException e) {
                    throw new FunctionInvocationException(e.getMessage());
                }

                Histogram h = Histogram.build(array);
                DM_Map info = new DM_Map();
                info.set("type", "hist");
                info.set("title", title);
                info.set("values", h.getValues());
                info.set("bins", h.getBins());

                return info;
            }
        });

        ctx.addFunction("stacked", new Function() {

            public DM_Map call(DM_String title, DM_Array values, DM_Array labels) throws FunctionInvocationException {

                Double[] means = new Double[values.size()];

                try {
                    for (int i = 0; i < values.size(); i++) {
                        means[i] = StatsAdapter.mean(values.get(i));
                    }
                } catch (StatsAdapterException e) {
                    throw new FunctionInvocationException(e.getMessage());
                }

                DM_Map info = new DM_Map();
                info.set("type", "stacked");
                info.set("title", title);
                info.set("values", means);
                info.set("labels", labels);

                return info;
            }
        });

        ctx.addFunction("column", new Function() {

            public DM_Map call(DM_String title, DM_Array values, DM_Array labels, DM_String legend) throws FunctionInvocationException {

                Double[] means = new Double[values.size()];

                try {
                    for (int i = 0; i < values.size(); i++) {
                        means[i] = StatsAdapter.mean(values.get(i));
                    }
                } catch (StatsAdapterException e) {
                    throw new FunctionInvocationException(e.getMessage());
                }

                DM_Map info = new DM_Map();
                info.set("type", "column");
                info.set("title", title);
                info.set("values", means);
                info.set("labels", labels);
                info.set("legend", legend);

                return info;
            }

            public DM_Map call(DM_String title, DM_Array values, DM_Array labels) throws FunctionInvocationException {
                return call(title, values, labels, null);
            }
        });
    }
}
