package mvc.view

import mvc.model.channel.DuplexChannelFactory
import mvc.model.channel.HalfDuplexChannelFactory
import mvc.model.channel.SimplexChannelFactory
import common.Config
import javax.swing.*
import javax.swing.BoxLayout.Y_AXIS

class SettingsPanel(networkPanel: NetworkPanel) : JPanel() {
    private val cfg = Config.config

    init {
        layout = BoxLayout(this, Y_AXIS)

        createScroll("Ширина", "graphW", 800, 10000)
        createScroll("Висота", "graphH", 800, 10000)

        createScroll("Час серії тестів", "ticks", 100, 10000)
        createScroll("Розмір повідомлення", "message", 256, 8192, 512)
        createScroll("Розмір пакету", "package", 128, 2048, 128)
        createScroll("Затримка повідомлення", "delay", 1, 1000)

        createChannel()

        val button = JButton("Застосувати")
        button.addActionListener { networkPanel.update() }
        add(button)
    }

    private fun createChannel() {
        add(JLabel("Канал"))
        val comboBox = JComboBox(arrayOf(SimplexChannelFactory(), HalfDuplexChannelFactory(), DuplexChannelFactory()))
        comboBox.selectedItem = cfg.getProperty("channelFactory")
        comboBox.addItemListener {
            cfg.setProperty("channelFactory", comboBox.selectedItem)
        }
        add(comboBox)
    }

    private fun createScroll(name: String, cfgName: String, min: Int, max: Int, increment: Int = max / 20) {
        val label = JLabel(name)
        add(label)
        val slider = JSlider(min, max)
        slider.createStandardLabels(increment)
        slider.addChangeListener {
            val value = slider.value
            cfg.setProperty(cfgName, value)
            label.text = "$name: $value"
        }
        slider.value = cfg.getInt(cfgName)
        add(slider)
    }
}
