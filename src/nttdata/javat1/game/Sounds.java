package nttdata.javat1.game;

import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JFrame;

/**
 * Pinball
 * 
 * Clase Sounds
 * 
 * @author Franco Mancinelli
 *
 */
public class Sounds {

	/* ~~ ATRIBUTOS */
	private JFrame frame;

	/**
	 * Crea/Construye la aplicación
	 */
	public Sounds() {
		initialize();
	}

	/**
	 * Inicializa un frame que permanecerá invisible.
	 * Esto permitirá ejecutar sonidos mientras se
	 * reproduce la aplicación en general.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(false);
	}
	
	/**
	 * Ejecuta un efecto de sonido
	 * @param url Url donde se encuentra ubicado el efecto de sonido
	 */
	public void ejecutarSonido(String url) {
		try {
			Clip sonido = AudioSystem.getClip();
			
			File f = new File(url);
			sonido.open(AudioSystem.getAudioInputStream(f));
			sonido.start();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	


}
