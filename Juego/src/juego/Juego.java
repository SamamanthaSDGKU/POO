package juego;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import control.Teclado;
import graficos.Pantalla;

public class Juego extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;
	private static final int ANCHO = 800; // constantes
	private static final int ALTO = 600;

	private static volatile boolean enFuncionamiento = false; // no podrá
																// ejecutarse de
																// forma simultanea por los dos threads
	private static Teclado teclado; 

	private static int x = 0; // inicia los valores de x, y
	private static int y = 0;

	private static final String NOMBRE = "Juego";
	private static JFrame ventana;
	private static Thread thread;
	private static Pantalla pantalla;

	private static BufferedImage imagen = new BufferedImage(ANCHO, ALTO, BufferedImage.TYPE_INT_RGB); // se
																							// hizo
																										// una
																										// imagen
																										// en
																										// blanco
	private static int[] pixeles = ((DataBufferInt) imagen.getRaster().getDataBuffer()).getData(); // convertir
																									// una
																									// imgen
																									// a
																									// numeros
																									// (para
																									// leer
																									// pixeles)
	public static final ImageIcon icono = new ImageIcon(Juego.class.getResource("/icono/icono.png"));

	private int aps = 0;
	private int fps = 0;

	private Juego() {
		setPreferredSize(new Dimension(ANCHO, ALTO));

		teclado = new Teclado();
		addKeyListener(teclado); // detectar lo que pulses
		pantalla = new Pantalla(ANCHO, ALTO);
		ventana = new JFrame(NOMBRE);
		ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ventana.setResizable(false);
		ventana.setIconImage(icono.getImage()); // para poner el icono
		ventana.setLayout(new BorderLayout());
		ventana.add(this, BorderLayout.CENTER); // agregar el canvas en el
												// centro de la ventana, para el
												// usuario sólo habrá un canvas

		ventana.pack();// el contenido se ajuste al tamaño que tenemos 800x600
		ventana.setLocationRelativeTo(null);
		ventana.setVisible(true);
	}

	public static void main(String[] args) {
		Juego juego = new Juego();
		juego.start();
	}

	public synchronized void start() // no se ejecuten a la vez
	{
		enFuncionamiento = true; // delante de la creacion del thread
		thread = new Thread(this, "Graficos");// pasar la clase que ejecutará
		thread.start();
		
	}

	public synchronized void stop() {
		enFuncionamiento = false;
		try {
			thread.join(); //
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void actualizar() {
		// actualizar variables de juego vidas, posicion, enemigos
		teclado.actualizar();

		// tecla pulsada

		if (teclado.arriba) {
			y--;
			System.out.println("Arriba");
		}
		if (teclado.abajo) {
			y++;
			System.out.println("Abajo");
		}
		if (teclado.izquierda) {
			x--;
			System.out.println("Izquierda");
		}
		if (teclado.derecha) {
			x++;
			System.out.println("Derecha");
		}

		aps++;
	}

	private void mostrar() { // re-dibujar los gráficos
		BufferStrategy estrategia = getBufferStrategy();

		if (estrategia == null) {
			createBufferStrategy(3);
			return;
		}

		pantalla.limpiar();
		pantalla.mostrar(x, y);// son las variables que se manipulará con el
								// teclado

		System.arraycopy(pantalla.pixeles, 0, pixeles, 0, pixeles.length);// copiar
																			// de
																			// la
																			// pantalla
																			// al
																			// juego
		Graphics g = estrategia.getDrawGraphics();
		g.drawImage(imagen, 0, 0, getWidth(), getHeight(), null);
		g.setColor(Color.white);
		g.fillRect(ANCHO / 2, ALTO / 2, 32, 32);
		g.dispose();// destruye g
		estrategia.show();
		fps++;
	}

	public void run() {
		// ejecutará el segundo thread
		System.out.println("El thread 2 se está ejecutando con exito");
		final int NS_POR_SEGUNDO = 1000000000;
		final byte APS_OBJETIVO = 60; // Actualizaciones por segundo
		final double NS_POR_ACTUALIZACION = NS_POR_SEGUNDO / APS_OBJETIVO;

		long referenciaActualizacion = System.nanoTime();
		long referenciaContador = System.nanoTime();

		double tiempoTranscurrido;
		double delta = 0;

		requestFocus();

		while (enFuncionamiento) {
			final long inicioBucle = System.nanoTime();

			tiempoTranscurrido = inicioBucle - referenciaActualizacion;
			referenciaActualizacion = inicioBucle;

			delta += tiempoTranscurrido / NS_POR_ACTUALIZACION;
			while (delta >= 1) {
				actualizar();
				delta--;
			}
			mostrar();
			if (System.nanoTime() - referenciaContador > NS_POR_SEGUNDO) {
				ventana.setTitle(NOMBRE + "--APS: " + aps + "--FPS: " + fps);
				aps = 0;
				fps = 0;
				referenciaContador = System.nanoTime();
			}

		}
	}
}
