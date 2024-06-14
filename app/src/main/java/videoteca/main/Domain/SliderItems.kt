package videoteca.main.Domain

/**
 * Classe che rappresenta un singolo elemento slider contenente un'immagine.
 *
 * @property image Risorsa dell'immagine da visualizzare nello slider.
 * @constructor Crea un'istanza di SliderItems con un'immagine specificata.
 */
class SliderItems(private var image: Int) {

    /**
     * Restituisce la risorsa dell'immagine corrente.
     *
     * @return Intero rappresentante la risorsa dell'immagine.
     */
    fun getImage(): Int {
        return image
    }

    /**
     * Imposta la risorsa dell'immagine con quella fornita.
     *
     * @param image Intero rappresentante la nuova risorsa dell'immagine da impostare.
     */
    fun setImage(image: Int) {
        this.image = image
    }
}
