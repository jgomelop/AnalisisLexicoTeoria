package codigo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;


public class FrmPrincipal {
    private JTextField txtEntrada;
    private JButton btnAnalizar;
    private JTextArea txtResultado;
    private JPanel panelMain;

    public FrmPrincipal() {
        btnAnalizar.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            File archivo = new File("archivo.txt");
            PrintWriter escribir;

            /*
            Simbolos de la pila: {z0, (, )}
            Estados del sistema = {0,1,2,3,4,5,6,7,8}
             */
            Deque<String> stack = new ArrayDeque<String>();
            stack.push("¬");

            int estado = 0;

            try {
                escribir = new PrintWriter(archivo);
                escribir.print(txtEntrada.getText());
                escribir.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }

            try {
                Reader lector = new BufferedReader(new FileReader("archivo.txt"));
                Lexer lexer = new Lexer(lector);
                String resultado = "";

                String msg;
                while (true) {

                    Tokens tokens = lexer.yylex();
                    msg = "Tamaño: "+ stack.size()
                            + " Tope: "+stack.peek()
                            +" estado: "+ estado;
                    System.out.println(msg);

                    if (tokens == null) {
                        resultado += "FIN\n";

                        switch (estado){
                            case 0: case 1: case 7: case 6:
                                try {
                                    stack.pop();
                                }catch (Exception ex){
                                    throw new RuntimeException("Algo ha salido mal");
                                }
                                break;
                            default:
                                estado = 8;
                                break;
                        }
                        System.out.println(estado);
                        if (!stack.isEmpty() || estado == 8) {
                            resultado += "Syntax Error\n";
                            txtResultado.setText(resultado);
                            return;
                        }
                        txtResultado.setText(resultado);
                        return;
                    }

                    switch (tokens) {
                        case ERROR:
                            resultado += "Simbolo no definido\n";
                            estado = 8;
                            break;
                        case Entero: case Float:
                            resultado += lexer.lexeme + ": Es " + tokens + "\n";

                            switch(estado){
                                case 0: case 2:
                                    estado = 1;
                                    break;
                                case 3:
                                    estado = 4;
                                    break;
                                case 5:
                                    if (stack.peek().charAt(0) != '('){
                                        estado = 8;
                                        break;
                                    }
                                    estado = 6;
                                    break;
                                case 7:
                                    if (stack.peek().charAt(0) != '¬'){
                                        estado = 8;
                                        break;
                                    }
                                    estado = 6;
                                    break;
                                default:
                                    estado = 8;
                                    break;
                            }
                            break;

                        case ParentesisApertura:
                            resultado += "Apertura "+lexer.yytext()+" -> "+tokens + "\n";
                            switch (estado){
                                case 0: case 2: case 7:
                                    estado = 3;
                                    stack.push("(");
                                    break;
                                case 3: case 5:
                                    if (stack.peek().charAt(0) != '('){
                                        estado = 8;
                                        break;
                                    }
                                    estado = 3;
                                    stack.push("(");
                                    break;
                                default:
                                    estado = 8;
                                    break;
                            }
                            break;

                        case ParentesisCierre:
                            if (stack.peek().charAt(0) != '('){
                                estado = 8;
                                break;
                            }

                            switch (estado){
                                case 6:
                                case 4:
                                    resultado += "Cierre "+lexer.yytext()+" -> "+tokens + "\n";
                                    stack.pop();
                                    //estado = 7;
                                    break;
                                default:
                                    estado = 8;
                                    break;
                            }
                            break;

                        default:
                            resultado += "Token "+lexer.yytext()+" -> "+tokens + "\n";
                            switch (estado){
                                case 1:
                                    estado = 2;
                                    break;
                                case 4:
                                    estado = 5;
                                    break;
                                case 6:
                                    if (stack.peek().charAt(0) == '('){
                                        estado = 5;
                                        break;
                                    } else if (stack.peek().charAt(0) == '¬') {
                                        estado = 7;
                                        break;
                                    } else{
                                        estado = 8;
                                        break;
                                    }
                                default:
                                    estado = 8;
                                    break;
                            }
                            break;
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(FrmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    });
}
    public static void main(String[] args) {
        JFrame framePrincipal = new JFrame("FrmPrincipal");
        framePrincipal.setContentPane(new FrmPrincipal().panelMain);
        framePrincipal.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        framePrincipal.pack();
        framePrincipal.setVisible(true);
    }

}
