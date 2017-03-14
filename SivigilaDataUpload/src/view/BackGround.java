/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author fabianandres
 */
public class BackGround extends javax.swing.JPanel implements Serializable{
    
    /**
     * Creates new form Fondo
     */
    public static String dir=System.getProperty("user.dir");
    
    
    private String name;
    private int x, y,width,height;
    
    /**
     * carga el fondo del frame
     * @param width ancho frame imagen
     * @param height alto frame imagen
     * @param name nombre imagen
     * @param x cooredenada
     * @param y coordenada
     */
    public BackGround(int width,int height,String name,int x,int y) {
        
        initComponents();
        this.setBounds(x, y, width,height);//coloca en x,y el panel y lo crea del tam dado
        this.x=x;
        this.y=y;   
        this.width=width;
        this.height=height;
        this.name=name;
        /*
        try {
            
            InputStream fnt_stream = getClass().getResourceAsStream("/avgardm.ttf");
            //File file = new File(".\\Fonts\\avgardm.ttf");
            font = Font.createFont(Font.TRUETYPE_FONT, fnt_stream).deriveFont(12f);
        } catch (FontFormatException ex) {
            Logger.getLogger(BackGround.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BackGround.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }
    
    @Override
    public void paintComponent(Graphics g){
        URL url = ClassLoader.getSystemClassLoader().getResource("images/"+name);
        ImageIcon image = new ImageIcon(url);
        //ImageIcon image=new ImageIcon(new ImageIcon((dir+"\\Imagenes\\"+name)).getImage());
        g.drawImage(image.getImage(),x,y, width,height,null);
       
        setOpaque(false);
        super.paintComponent(g);
    }
    
    /**
     * carga una imagen
     * @param ruta ruta de la imagen
     * @param ancho ancho de la imagen
     * @param largo largo de la imagen
     * @return
     */
    public static Icon darImagen(String ruta, int ancho, int largo) {
		ImageIcon imagen = new ImageIcon(new ImageIcon((ruta)).getImage());
		Icon icono = new ImageIcon(imagen.getImage().getScaledInstance(ancho,
				largo,Image.SCALE_SMOOTH));
		return icono;
                
    }
    /**
     * fuente usada en la aplicacion
     */
    public static Font font;
     
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

}
