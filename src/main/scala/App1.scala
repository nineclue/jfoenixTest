import javafx.application.Application
import javafx.stage.Stage
import javafx.scene.{Scene, Group}
import javafx.scene.layout.{FlowPane, StackPane}
import javafx.scene.control.Button
import javafx.geometry.Insets
import com.jfoenix.controls.JFXButton

object App1:
    def main(as: Array[String]): Unit = 
        Application.launch(classOf[App1], as:_*)

class App1 extends Application:
    override def start(ps: Stage) = 
        val main = FlowPane()
        main.setVgap(20)
        main.setHgap(20)

        val rbtn = JFXButton("Raised 버튼")
        rbtn.getStyleClass().add("button-raised")
        val dbtn = JFXButton("Disabled 버튼")
        dbtn.setDisable(true)

        main.getChildren.addAll(
            Button("JavaFx 버튼"), 
            JFXButton("JFoenix 버튼"),
            rbtn, dbtn)

        val pane = StackPane()
        pane.getChildren.add(main)
        StackPane.setMargin(main, Insets(100))
        pane.setStyle("-fx-background-color:WHITE")

        val scene = Scene(pane, 800, 200)
        scene.getStylesheets().add(getClass.getResource("/css/jfoenix-components.css").toExternalForm())
        ps.setTitle("JavaFX 좀!!!")
        ps.setScene(scene)

        ps.show
