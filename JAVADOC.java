import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * essa classe é responsável por representar um usuário e realizar operações relacionadas ao banco de dados,
 * como conexão e verificação de login.
 */
public class User {
    // começo da configuração do banco de dados
    private static final String DB_URL = "jdbc:mysql://127.0.0.1:3306/test"; // aqui fica a URL do banco de dados
    private static final String DB_USER = "lopes"; // inserir usuário do banco de dados
    private static final String DB_PASSWORD = "123"; // inserir senha do banco de dados

    /**
     * aqui começamos a conexão ao banco de dados usando as configurações predefinidas.
     *
     * @return o return nos mostra um objeto {@link Connection} representando a conexão com o banco de dados, 
     * ou {@code null} caso a conexão falhe.
     */
    public Connection conectarBD() {
        Connection conn = null;
        try {
            // conexão com o banco de dados usando DriverManager
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            // mostra uma mensagem de erro caso a conexão falhe
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
        return conn; // return do Connection
    }

 
    public String nome = ""; // nome do usuário encontrado no banco de dados
    public boolean result = false; // mostra o resultado da verificação de login

    /**
     * verifica se um usuário existe no banco de dados com o login e a senha fornecidos.
     *
     * @param login O nome de usuário a ser autenticado.
     * @param senha A senha correspondente ao usuário.
     * @return {@code true} se o usuário foi autenticado com sucesso, {@code false} caso contrário.
     */
    public boolean verificarUsuario(String login, String senha) {
        // consulta o SQL usando placeholders (?) para evitar a injeção de SQL
        String sql = "SELECT nome FROM usuarios WHERE login = ? AND senha = ?";

        // try para gerenciar automaticamente os recursos
        try (Connection conn = conectarBD(); // tem a conexão com o banco de dados
             PreparedStatement pst = conn.prepareStatement(sql)) { // prepara a consulta SQL

            // verifica se a conexão foi bem-sucedida - boolean
            if (conn == null) {
                System.err.println("Conexão falhou!");
                return false;
            }

            // substitui os placeholders (?) pelos valores fornecidos
            pst.setString(1, login);
            pst.setString(2, senha);

            // executa a consulta e processa os resultados
            try (ResultSet rs = pst.executeQuery()) {
                // se tiver um resultado, o usuário é válido
                if (rs.next()) {
                    result = true; // atualiza o resultado para true - boolean
                    nome = rs.getString("nome"); // acha o nome do usuário
                }
            }

        } catch (Exception e) {
            // mostra uma mensagem de erro caso ocorra uma falha
            System.err.println("Erro ao verificar usuário: " + e.getMessage());
        }

        // return true se o usuário for encontrado, ou false caso contrário - boolean
        return result;
    }
}
