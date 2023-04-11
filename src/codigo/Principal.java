package codigo;

import java.io.File;

public class Principal {
    public static void main(String[] args) {
        String path = new File("src/codigo/Lexer.flex").getAbsolutePath();
        generarLexer(path);
    }
    public static void generarLexer(String ruta){
        File archivo = new File(ruta);
        JFlex.Main.generate(archivo);
    }
}