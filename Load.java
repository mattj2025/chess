import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Load implements ActionListener
{
    private Game game;

    @Override
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
            System.out.println("FileNotFoundException");
        } catch (IOException e1) {
            System.out.println("IOException");
        } catch (ClassNotFoundException e1) {
            System.out.println("ClassNotFoundException");
        }
    }
}
