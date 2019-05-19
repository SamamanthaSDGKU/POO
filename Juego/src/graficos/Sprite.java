package graficos;

public final class Sprite {
	// definir tamaño del sprite
	private final int lado;

	// coordenada
	private int x;
	private int y;

	public int[] pixeles; // guarda el sprite
	private HojaSprites hoja; // carga la hoja de sprites

	// coleccion de sprites
	public static Sprite arnold = new Sprite(32, 0, 0, HojaSprites.mapa1);
	// fin de la coleccion

	public Sprite(int lado, int columna, int fila, final HojaSprites hoja) {
		this.lado = lado;
		pixeles = new int[lado * lado];
		this.x = columna * lado;
		this.y = fila * lado;
		this.hoja = hoja;

		for (int y = 0; y < lado; y++) {
			for (int x = 0; x < lado; x++) {
				pixeles[x + y * lado] = hoja.pixeles[(x + this.x) + (y + this.y) * hoja.getAncho()];
			}
		}

	}
}
