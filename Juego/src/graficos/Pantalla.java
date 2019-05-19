package graficos;

public final class Pantalla {
	private final int ancho;
	private final int alto;
	public final int[] pixeles;

	// temporal
	private static final int LADO_SPRITE = 32;
	private static final int MASCARA_SPRITE = LADO_SPRITE - 1;
	// fin de temporal

	public Pantalla(final int ancho, final int alto) {
		this.ancho = ancho;
		this.alto = alto;
		pixeles = new int[ancho * alto];
	}

	public void limpiar() {
		for (int i = 0; i < pixeles.length; i++) {
			pixeles[i] = 0; // se pinta en negro hexadecimal

		}
	}

	public void mostrar(final int compensacionX, final int compensasionY) {
		for (int y = 0; y < alto; y++) {
			int posicionY = y + compensasionY;
			if (posicionY < 0 || posicionY >= alto) {
				// para no salirnos del mapa
				continue; // se sale del ciclo

			}

			for (int x = 0; x < ancho; x++) {
				int posicionX = x + compensacionX;

				if (posicionX < 0 || posicionX >= ancho) {
					// para no salirnos del mapa
					continue; // se sale del ciclo

				}
				// temporal
				pixeles[posicionX + posicionY * ancho] = Sprite.arnold.pixeles[(x & MASCARA_SPRITE)
						+ (y & MASCARA_SPRITE) * LADO_SPRITE];
			}

		}

	}

}
