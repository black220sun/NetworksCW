package mvc.view

import mvc.model.channel.ChannelFactory
import mvc.model.node.Node
import mvc.controller.Network
import common.Config
import common.WeightList
import mvc.controller.Informator

import javax.swing.*
import java.awt.*

class Toolbar(private val networkPanel: NetworkPanel) : JPanel() {
    private val network: Network = networkPanel.network
    private val fromNode: JComboBox<Node>
    private val toNode: JComboBox<Node>
    private val timeLabel: JLabel
    private val infoArea: JTextArea

    init {
        layout = BoxLayout(this, BoxLayout.Y_AXIS)
        val nodes = network.getNodes().toTypedArray()
        fromNode = JComboBox(nodes)
        toNode = JComboBox(nodes)
        addNodePanel()
        addChangePanel()
        addInfoPanel()
        timeLabel = JLabel("0")
        infoArea = Config.config.getProperty("info")
        infoArea.isEditable = false
        add(infoArea)
    }

    private fun addInfoPanel() {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.X_AXIS)
        panel.add(createButton("Видалити всі вузли") {
            Config.config.setProperty("counter", 0)
            network.clear()
            updateNodes()
            networkPanel.update()
        })
        panel.add(createButton("Нова мережа") {
            Config.config.setProperty("counter", 0)
            network.generateNetwork()
            updateNodes()
            networkPanel.update()
        })
        panel.add(createButton("Табл.маршрутизації") {
            val node = fromNode.selectedItem as Node
            infoArea.text = network.getPaths(node)
                    .joinToString("\n") { it.toString() }
            Config.config.getProperty<JTabbedPane>("tab").selectedIndex = 2
        })
        panel.add(createButton("Серія тестів") {
            infoArea.text = ""
            network.closeAll()
            infoArea.text = Informator.configOptions +
                    Informator(network).runTests()
            Config.config.getProperty<JTabbedPane>("tab").selectedIndex = 2
        })
        add(panel)
    }

    private fun addChangePanel() {
        val pane = JPanel()
        pane.layout = BoxLayout(pane, BoxLayout.X_AXIS)
        pane.add(createButton("Роб. станція", nodeAction { n1, _ ->
            n1.isTerminal = !n1.isTerminal
            updateNodes()
        }))
        pane.add(createButton("Обрати", nodeAction { from, to ->
            val current = from.isSelected
            if (current == to.isSelected) {
                val changed = !current
                network.getPath(from, to).forEach { ch -> ch.isSelected = changed }
                from.isSelected = changed
                to.isSelected = changed
            }
        }))
        pane.add(createButton("Новий вузол") {
            network.addNode()
            updateNodes()
            networkPanel.update()
        })
        pane.add(createButton("Видалити вузол", nodeAction { node, _ ->
            network.removeNode(node)
            updateNodes()
        }))
        pane.add(createButton("Додати канал", nodeAction { n1, n2 ->
            val cfg = Config.config
            val weight = cfg.getProperty<WeightList>("weights").weight
            cfg.getProperty<ChannelFactory>("channelFactory").createChannel(n1, n2, weight)
        }))
        pane.add(createButton("Видалити канал", nodeAction { from, to ->
            network.removeConnection(from, to) }))
        add(pane)
    }

    private fun addNodePanel() {
        val panel = JPanel()
        panel.layout = BoxLayout(panel, BoxLayout.X_AXIS)
        panel.add(fromNode)
        panel.add(toNode)
        panel.add(createButton("З'єднання", nodeAction { from, to ->
            network.createConnection(from, to)
        }))
        panel.add(createButton("Роз'єднання", nodeAction { from, to ->
            network.closeConnection(from, to)
        }))
//        panel.add(createButton("Роз'єднати все") {
//            mvc.controller.closeAll()
//            networkPanel.update()
//        })
        add(panel)
    }

    private fun createButton(name: String, action: () -> Unit): Component {
        val button = JButton(name)
        button.addActionListener { action() }
        return button
    }

    private fun updateNodes() {
        fromNode.removeAllItems()
        toNode.removeAllItems()
        network.getNodes().forEach {
            fromNode.addItem(it)
            toNode.addItem(it)
        }
    }

    private fun nodeAction(consumer: (Node, Node) -> Unit): () -> Unit {
        return {
            val from = fromNode.selectedItem as Node
            val to = toNode.selectedItem as Node
            consumer(from, to)
            networkPanel.update()
        }
    }
}
