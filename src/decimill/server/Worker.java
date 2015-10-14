package decimill.server;

import decimill.model.Model;
import decimill.model.ModelCompiler;
import decimill.model.Node;
import decimill.parser.CompilerException;
import java.io.File;
import jviz.Dot;
import jviz.JvizException;

/**
 * @author David
 */
public class Worker {

    public static String compileModel(int id, String namespace, String text)
            throws CompilerException, JvizException {

        Model model = new Model(id, namespace);
        String compiled = ModelCompiler.compile(model, text);

        Dot dot = new Dot();
        File dir = new File("img/" + model.getId());

        if (!dir.exists()) {
            dir.mkdir();
        } else {
            for (File file : dir.listFiles()) {
                file.delete();
            }
        }

        if (model.hasNodes()) {
            dot.png(model.toDot(), "img/" + model.getId() + "/full.png");
        }

        for (Node node : model.getNodes().values()) {
            dot.png(node.toDot(), "img/" + model.getId() + "/" + node.getId() + ".png");
        }

        return compiled;
    }
}
