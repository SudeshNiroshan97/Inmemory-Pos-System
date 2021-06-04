import DB.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

public class POSAppInitializer extends Application {

    public static void main(String[] args) {
        launch(args);
        try {
            DBConnection.getInstance().getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        URL resource = this.getClass().getResource("/views/DashboardForm.fxml");

        Parent root = FXMLLoader.load(resource);

        Scene DashboardScene=new Scene(root);
        primaryStage.setScene(DashboardScene);
        primaryStage.show();





       // Stage primaryStage=(Stage)(this.root.getScene().getWindow());

    }
}
