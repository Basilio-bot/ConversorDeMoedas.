import java.net.HttpURLConnection;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.Scanner;
import org.json.JSONObject;

public class ConversorMoedas {

    // Faz requisição à API de câmbio
    public static double obterTaxaCambio(String de, String para) {
        try {
            String url_str = "https://v6.exchangerate-api.com/v6/082bfd8255f105add3dee9d8/latest/" + de;

            URL url = new URL(url_str);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.setRequestMethod("GET");
            request.connect();

            Scanner scanner = new Scanner(request.getInputStream());
            StringBuilder inline = new StringBuilder();

            while (scanner.hasNext()) {
                inline.append(scanner.nextLine());
            }
            scanner.close();

            JSONObject json = new JSONObject(inline.toString());

            // Verifica se a resposta foi bem-sucedida
            if (!json.getString("result").equals("success")) {
                System.out.println("Erro: resposta inválida da API.");
                return -1;
            }

            JSONObject rates = json.getJSONObject("conversion_rates");
            return rates.getDouble(para);

        } catch (Exception e) {
            System.out.println("Erro na conexão com a API.");
            return -1;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\nBem-vindo ao Conversor de Moedas!");
            System.out.println("Este programa utiliza dados atualizados em tempo real para converter moedas.");
            System.out.println("\n1) Dólar => Peso argentino");
            System.out.println("2) Peso argentino => Dólar");
            System.out.println("3) Dólar => Real brasileiro");
            System.out.println("4) Real brasileiro => Dólar");
            System.out.println("5) Dólar => Peso colombiano");
            System.out.println("6) Peso colombiano => Dólar");
            System.out.println("7) Sair");

            // Validação da opção do menu
            while (true) {
                System.out.print("Escolha uma opção: ");
                if (scanner.hasNextInt()) {
                    opcao = scanner.nextInt();
                    break;
                } else {
                    System.out.println("Entrada inválida. Por favor, digite um número de 1 a 7.");
                    scanner.next(); // limpa a entrada inválida
                }
            }

            String de = "", para = "";

            switch (opcao) {
                case 1: de = "USD"; para = "ARS"; break;
                case 2: de = "ARS"; para = "USD"; break;
                case 3: de = "USD"; para = "BRL"; break;
                case 4: de = "BRL"; para = "USD"; break;
                case 5: de = "USD"; para = "COP"; break;
                case 6: de = "COP"; para = "USD"; break;
                case 7: System.out.println("Saindo..."); continue;
                default:
                    System.out.println("Opção inválida.");
                    continue;
            }

            // Validação do valor a ser convertido
            double valor = 0;
            while (true) {
                System.out.print("Digite o valor: ");
                if (scanner.hasNextDouble()) {
                    valor = scanner.nextDouble();
                    if (valor < 0) {
                        System.out.println("Por favor, insira um valor positivo.");
                    } else {
                        break;
                    }
                } else {
                    System.out.println("Entrada inválida. Por favor, insira um número.");
                    scanner.next(); // limpa a entrada inválida
                }
            }

            double taxa = obterTaxaCambio(de, para);
            if (taxa != -1) {
                double resultado = valor * taxa;
                System.out.printf("Resultado: %.2f %s = %.2f %s%n", valor, de, resultado, para);
            } else {
                System.out.println("Erro ao obter a taxa de câmbio.");
            }

        } while (true); // loop só para se o usuário digitar 7
    }
}
