import java.util.Map;
import java.util.HashMap;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class ScrabbleScore {
  public static void main(String[] args) {
    String layout = "templates/layout.vtl";

    get("/", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/home.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

    get("detector", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/detector.vtl");

    String word = request.queryParams("word");
    Integer score = scoreTotal(word);

    model.put("score", score);
    return new ModelAndView(model, layout);

    }, new VelocityTemplateEngine());

    get("/numberWords", (request, response) -> {
      Map<String, Object> model = new HashMap<String, Object>();
      model.put("template", "templates/numberWords.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

  }

    public static Integer countScore(char letter) {
      if ( "aeioulnrst".indexOf(letter) >= 0) {
        return 1;
      } else if ("dg".indexOf(letter) >= 0) {
        return 2;
      } else if ("bcmp".indexOf(letter) >= 0) {
        return 3;
      } else  if ("fhvwy".indexOf(letter) >= 0) {
        return 4;
      } else if ("k".indexOf(letter) >= 0) {
        return 5;
      } else if ("jx".indexOf(letter) >= 0) {
        return 8;
      } else {
        return 10;
      }
    }

    public static Integer scoreTotal(String userInput) {
      int score = 0;
      for (char s: userInput.toCharArray()) {
        score = score + countScore(s);
      }
      return score;
    }

    public static String numberTranslator(Integer inputNumber) {
      String[] singleDigit = {"zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten"};
      String[] teenDigit = {"eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
      String[] doubleDigit = {"twenty", "thirty", "fourty", "fifty", "sixty", "seventy", "eighty", "ninty"};
      if (inputNumber < 10) {
        return singleDigit[inputNumber];
      } else if ((inputNumber > 10) && (inputNumber <= 19)) {
          return teenDigit[inputNumber - 11];
      } else  if ((inputNumber > 19) && (inputNumber <= 99) && (inputNumber % 10 == 0)) {
          int firstDigit = Integer.parseInt(Integer.toString(inputNumber).substring(0, 1));
          return doubleDigit[firstDigit - 2];
      } else if ((inputNumber > 19) && (inputNumber <= 99) && (inputNumber % 10 != 0)) {
          int firstDigit = Integer.parseInt(Integer.toString(inputNumber).substring(0, 1));
          int secondDigit = Integer.parseInt(Integer.toString(inputNumber).substring(1, 2));
          return doubleDigit[firstDigit - 2] + " " + singleDigit[secondDigit];
      } else if ((inputNumber > 99) && (inputNumber < 999)) {
          int firstDigit = Integer.parseInt(Integer.toString(inputNumber).substring(0, 1));
          int secondDigit = Integer.parseInt(Integer.toString(inputNumber).substring(1, 2));
          int thirdDigit = Integer.parseInt(Integer.toString(inputNumber).substring(2, 3));
          return singleDigit[firstDigit] + " hundred " + doubleDigit[secondDigit -2] + " " + singleDigit[thirdDigit];
      } else {
        return "asd";
      }

    }

    public String wholeNumberTranslator (Integer number){
      String numeric = "";
      String convertedNumber = Integer.toString(number);
      if (convertedNumber.length() < 3) {
        numeric = numberTranslator(number);
      } else if ((Integer.parseInt(convertedNumber.substring(1, 2)) == 0) && (Integer.parseInt(convertedNumber.substring(2, 3)) == 0)) {
        numeric = numberTranslator(Integer.parseInt(convertedNumber.substring(0, 1))) + " hundred";
      } else if ((Integer.parseInt(convertedNumber.substring(1, 2)) == 0) && (Integer.parseInt(convertedNumber.substring(2, 3)) != 0)) {
        numeric = numberTranslator(Integer.parseInt(convertedNumber.substring(0, 1))) + " hundred" + " " + numberTranslator (Integer.parseInt(convertedNumber.substring(2, 3)));
      } else {
        numeric = numberTranslator(number);
      }
      return numeric;
    }
  }
