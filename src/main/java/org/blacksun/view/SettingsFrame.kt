package org.blacksun.view

import guru.nidi.graphviz.attribute.Color
import org.blacksun.graph.channel.DuplexChannelFactory
import org.blacksun.graph.channel.HalfDuplexChannelFactory
import org.blacksun.graph.channel.SimplexChannelFactory
import org.blacksun.utils.Config
import javax.swing.*

class SettingsFrame : JFrame("Settings") {
    private val cfg = Config.config
    private val colors = arrayOf(Color.BLACK, Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.BROWN, Color.CYAN, Color.MAGENTA, Color.GRAY, Color.VIOLET, Color.PURPLE, Color.ORANGE)

    init {
        defaultCloseOperation = WindowConstants.DISPOSE_ON_CLOSE
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.Y_AXIS)
        contentPane = panel
        createScroll("View width", "viewW", 400, 2000)
        createScroll("View height", "viewH", 400, 2000)
        createResize()
        createScroll("Graph width", "graphW", 800, 10000)
        createScroll("Graph height", "graphH", 800, 10000)

        createScroll("Min ticks for test", "ticks", 100, 10000)
        createScroll("Average message size", "message", 256, 8192, 512)
        createScroll("Package size", "package", 128, 2048, 128)
        createScroll("Message appearance delay", "delay", 1, 1000)
        createScroll("Message appearance amount", "amount", 1, 64)

        createChannel()

        createColor("Node color", "node")
        createColor("Channel color", "channel")
        createColor("Selected node color", "selectedN")
        createColor("Selected channel color", "selectedC")
        createColor("Terminal node color", "terminal")
        createColor("Connected channel color", "connected")

        isVisible = true
        pack()
    }

    private fun createChannel() {
        add(JLabel("Default channel"))
        val comboBox = JComboBox(arrayOf(SimplexChannelFactory(), HalfDuplexChannelFactory(), DuplexChannelFactory()))
        comboBox.selectedItem = cfg.getProperty("channelFactory")
        comboBox.addItemListener { e -> cfg.setProperty("channelFactory", comboBox.selectedItem) }
        add(comboBox)
    }

    private fun createColor(name: String, cfgName: String) {
        add(JLabel(name))
        val comboBox = JComboBox(colors)
        comboBox.selectedItem = cfg.getColor(cfgName)
        comboBox.addItemListener { e -> cfg.setProperty(cfgName, comboBox.selectedItem) }
        add(comboBox)
    }

    private fun createResize() {
        val checkBox = JCheckBox("Resize graph?")
        checkBox.isSelected = cfg.getBoolean("resize")
        checkBox.addChangeListener { e -> cfg.setProperty("resize", checkBox.isSelected) }
        add(checkBox)
    }

    private fun createScroll(name: String, cfgName: String, min: Int, max: Int, increment: Int = max / 20) {
        val label = JLabel(name)
        add(label)
        val slider = JSlider(min, max)
        slider.createStandardLabels(increment)
        slider.addChangeListener { e ->
            val value = slider.value
            cfg.setProperty(cfgName, value)
            label.text = "$name ($value)"
        }
        slider.value = cfg.getInt(cfgName)
        add(slider)
    }
}