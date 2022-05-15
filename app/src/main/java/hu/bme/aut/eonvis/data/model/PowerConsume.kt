package hu.bme.aut.eonvis.data.model

class PowerConsume (val id: Long, val incoming: Double, val outgoing: Double) {
    private val tagList = ArrayList<String>()

    fun addTags(tags: ArrayList<String>) {
        tagList.addAll(tags)
    }

    fun getTags(): ArrayList<String>{
        return tagList
    }
}