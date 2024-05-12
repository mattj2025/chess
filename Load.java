import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JFileChooser;

public class Load implements ActionListener
{
    private Game game;

    public void actionPerformed(ActionEvent e)
    {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
        "Serialized Files (.ser)", "ser");
        chooser.setFileFilter(filter);
        chooser.showOpenDialog(Main.jFrame);
        File file = chooser.getSelectedFile();

        try (FileInputStream fis = new FileInputStream(file); ObjectInputStream ois = new ObjectInputStream(fis)) {

            game = (Game) ois.readObject();
            
            ois.close();
            fis.close();
            
            game.loadGame();

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
    }
}
