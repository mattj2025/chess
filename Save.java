import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.JOptionPane;

public class Save implements ActionListener
{
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (Main.multiplayer)
            JOptionPane.showMessageDialog(null, "Cannot save LAN Games.");
        else
        {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream(); ObjectOutputStream oos = new ObjectOutputStream(bos)) 
            {
                oos.writeObject(new Game());
                File file = new File("saves\\" + JOptionPane.showInputDialog("Name of file:") + ".ser");
                
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    fos.write(bos.toByteArray());
                    fos.flush();
                }

            } catch (IOException e1) {
                System.out.println("Failed to Save");
            }
        }
    }
}
