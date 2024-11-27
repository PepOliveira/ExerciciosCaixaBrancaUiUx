import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
//imports .java

public class User {
//configuração do banco de dados
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/test";
    private static final String DB_USER = "lopes";
    private static final String DB_PASSWORD = "123";

//conectar banco de dados
    public Connection conectarBD() {
        Connection conn = null;
        try {
        //estabele conexão com o BD usando DriveManager
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
        //mensagem de erro caso a conexão falhe
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
        return conn; // retorna o Connection e pode ser null se a conexão falhar 
    }

    public String nome = ""; //nome de usuario
    public boolean result = false; //resultado pra verificação e vai ser True se a verificação for valida

    public boolean verificarUsuario
//aqui temos consulta no SQL com seus PlaceHolders para evitar injeção direta 
    (String login, String senha) {
        String sql = "SELECT nome FROM usuarios WHERE login = ? AND senha = ?";

//no try nós temos o fechamento connection,PreparedStatement e ResultSet
        try (Connection conn = conectarBD(); //conexão com o banco
             PreparedStatement pst = conn.prepareStatement(sql)) {

//verificação se a consulta foi bem sucedida
            if (conn == null) {
                System.err.println("Conexão falhou!");
                return false;
            }
//SQL usa `?` como um parâmetro,que são substituidos depois por Login e Senha por questões de seurança 

            pst.setString(1, login);
            pst.setString(2, senha);

//processo de resultado apos a consulta
            try (ResultSet rs = pst.executeQuery()) {
    //caso tenha resultado o usuario será valido
            if (rs.next()) {
                result = true; //atualiza o resultado para true
                    nome = rs.getString("nome");
                }
            }
//o bloco Catch inclui a mensagem de erro para o user quando o login e a verificação não dão certo 
        } catch (Exception e) {
            System.err.println("Erro ao verificar usuário: " + e.getMessage());
        }
        return result; //retorna True caso o usuario seja encontrado,se não for encontrado é False. Ação Booleana.
    }
}
