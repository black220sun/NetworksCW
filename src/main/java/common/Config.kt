package common

import guru.nidi.graphviz.attribute.Color
import mvc.model.channel.HalfDuplexChannelFactory
import java.util.*

class Config private constructor() {

    private val properties: HashMap<String, Any> = HashMap()

    init {
        properties["frameW"] = 2000
        properties["frameH"] = 1050
        properties["viewW"] = 1200
        properties["viewH"] = 1050
        properties["graphW"] = 1180
        properties["graphH"] = 980
        properties["resize"] = true
        properties["node"] = Color.GREEN
        properties["channel"] = Color.BLACK
        properties["selectedN"] = Color.RED
        properties["selectedC"] = Color.PINK
        properties["connected"] = Color.BLUE
        properties["terminal"] = Color.VIOLETRED
        properties["utility"] = 32 // package for VC initiation
        properties["package"] = 512 // package size
        properties["message"] = 2048 // average message size (+/- 50%)
        //properties.put("render", true);
        properties["ticks"] = 1000 // min ticks for a single test
        properties["delay"] = 25 // delay between messages appearance
        properties["amount"] = 1 // amount of appeared messages
        properties["channelFactory"] = HalfDuplexChannelFactory()
    }

    fun getColor(property: String): Color {
        return getProperty(property, Color.BLACK)
    }

    fun getInt(property: String): Int {
        return getProperty(property, 0)
    }

    fun getBoolean(property: String): Boolean {
        return getProperty(property, false)
    }

    fun <T> getProperty(property: String): T {
        return properties[property] as T
    }

    fun <T> getProperty(property: String, defaultValue: T): T {
        val value = getProperty<T>(property)
        return value ?: defaultValue
    }

    fun setProperty(property: String, value: Any) {
        properties[property] = value
    }

    companion object {
        val config = Config()
    }
}
