package codigo;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
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
                while (true) {
                    Tokens tokens = lexer.yylex();
                    if (tokens == null) {
                        resultado += "FIN";
                        txtResultado.setText(resultado);
                        return;
                    }
                    switch (tokens) {
                        case ERROR:
                            resultado += "Simbolo no definido\n";
                            break;
                        case Identificador: case Numero: case Reservadas:
                            resultado += lexer.lexeme + ": Es un " + tokens + "\n";
                            break;
                        default:
                            resultado += "Token: " + tokens + "\n";
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
