package hu.bme.aut.eonvis.data.model

class PowerConsume (val id: Long, var incoming: Double, var outgoing: Double, val tagList: ArrayList<String> = ArrayList()) {

    fun addTags(tags: ArrayList<String>) {
        tagList.addAll(tags)
    }

    fun getTags(): ArrayList<String>{
        return tagList
    }
}