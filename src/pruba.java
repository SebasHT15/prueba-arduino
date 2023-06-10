import java.util.Map;

public class pruba {
    public static void main(String args[]){
        String contra = SerialCommunication.recibirDato();
        System.out.println(contra);

        String compressedCode = HuffmanEncoder.compress(contra);
        System.out.println("Código comprimido: " + compressedCode);

        // Obtener el nodo raíz del árbol de Huffman
        Map<Character, Integer> frequencyMap = HuffmanEncoder.getCharacterFrequency(contra);
        HuffmanEncoder.HuffmanNode root = HuffmanEncoder.buildHuffmanTree(frequencyMap);

        // Descomprimir el código comprimido
        String decompressedText = HuffmanEncoder.decompress(compressedCode, root);
        System.out.println("Texto descomprimido: " + decompressedText);
    }
}
