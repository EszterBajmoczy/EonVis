package hu.bme.aut.eonvis.converter

import hu.bme.aut.eonvis.data.database.ArrayTypeConverter
import hu.bme.aut.eonvis.data.database.PowerConsumeDAO
import hu.bme.aut.eonvis.data.model.PowerConsume

class DataConverter {
    private var arrayTypeConverter = ArrayTypeConverter()

    fun ConvertToDTO(data: PowerConsume) : PowerConsumeDAO {
        return PowerConsumeDAO(data.id, data.incoming, data.outgoing, arrayTypeConverter.fromArray(data.getTags()))
    }

    fun ConvertToModel(data: PowerConsumeDAO) : PowerConsume {
        return PowerConsume(data.id, data.incoming, data.outgoing, arrayTypeConverter.toArray(data.tags))
    }
}