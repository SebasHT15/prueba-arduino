import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class HuffmanEncoder {
    private static class HuffmanNode implements Comparable<HuffmanNode> {
        char character;
        int frequency;
        HuffmanNode left;
        HuffmanNode right;

        public HuffmanNode(char character, int frequency, HuffmanNode left, HuffmanNode right) {
            this.character = character;
            this.frequency = frequency;
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(HuffmanNode other) {
            return this.frequency - other.frequency;
        }
    }

    public static Map<Character, String> compress(String input) {
        Map<Character, Integer> frequencyMap = getCharacterFrequency(input);
        HuffmanNode root = buildHuffmanTree(frequencyMap);
        Map<Character, String> huffmanCodes = generateHuffmanCodes(root);
        return huffmanCodes;
    }

    private static Map<Character, Integer> getCharacterFrequency(String input) {
        Map<Character, Integer> frequencyMap = new HashMap<>();
        for (char c : input.toCharArray()) {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
        }
        return frequencyMap;
    }

    private static HuffmanNode buildHuffmanTree(Map<Character, Integer> frequencyMap) {
        PriorityQueue<HuffmanNode> priorityQueue = new PriorityQueue<>();

        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            priorityQueue.add(new HuffmanNode(entry.getKey(), entry.getValue(), null, null));
        }

        while (priorityQueue.size() > 1) {
            HuffmanNode leftChild = priorityQueue.poll();
            HuffmanNode rightChild = priorityQueue.poll();

            int combinedFrequency = leftChild.frequency + rightChild.frequency;
            HuffmanNode parent = new HuffmanNode('\0', combinedFrequency, leftChild, rightChild);

            priorityQueue.add(parent);
        }

        return priorityQueue.poll();
    }

    private static Map<Character, String> generateHuffmanCodes(HuffmanNode root) {
        Map<Character, String> huffmanCodes = new HashMap<>();
        generateHuffmanCodesRecursive(root, "", huffmanCodes);
        return huffmanCodes;
    }

    private static void generateHuffmanCodesRecursive(HuffmanNode node, String code, Map<Character, String> huffmanCodes) {
        if (node == null) {
            return;
        }

        if (node.left == null && node.right == null) {
            huffmanCodes.put(node.character, code);
        }

        generateHuffmanCodesRecursive(node.left, code + "0", huffmanCodes);
        generateHuffmanCodesRecursive(node.right, code + "1", huffmanCodes);
    }
}