package videoteca.main.Domain

class SliderItems(private var image: Int) {

    fun getImage(): Int {
        return image
    }

    fun setImage(image: Int) {
        this.image = image
    }
}
