package tasks;

import common.Person;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
Далее вы увидите код, который специально написан максимально плохо.
Постарайтесь без ругани привести его в надлежащий вид
P.S. Код в целом рабочий (не везде), комментарии оставлены чтобы вам проще понять чего же хотел автор
P.P.S Здесь ваши правки необходимо прокомментировать (можно в коде, можно в PR на Github)
 */
public class Task9 {

  // Костыль, эластик всегда выдает в топе "фальшивую персону".
  // Конвертируем начиная со второй

  // Для пропуска первой персоны используем skip из SteamAPI. За пустой список персон можно не переживать,
  // skip выдаст пустой stream если количество элементов для пропуска больше длины stream.
  // Получаем имена при помощи map и собираем в список.
  public List<String> getNames(List<Person> persons) {
    return persons.stream().skip(1).map(Person::firstName).collect(Collectors.toList());
  }

  // Зачем-то нужны различные имена этих же персон (без учета фальшивой разумеется)
  // Используем конструктор Set'a. Как показывали на лекции :)
  public Set<String> getDifferentNames(List<Person> persons) {
    return new HashSet<>(getNames(persons));
  }

  // Тут фронтовая логика, делаем за них работу - склеиваем ФИО
  // При помощи Stream API можно легко преобразовать компоненты имени в stream, отфильтровать null значения
  // и собрать строку с нужным разделителем.
  // В изначальной имплементации secondName добавлялся два раза - в начале и в конце.
  // Изменил второй secondName на middleName.
  public String convertPersonToString(Person person) {
    return Stream.of(person.secondName(), person.firstName(), person.middleName())
        .filter(Objects::nonNull)
        .collect(Collectors.joining(" "));
  }

  // словарь id персоны -> ее имя
  // Используем Stream API для построения Map.
  // Коллектору задаем mergeFunction, которая оставляет существующий элемент Map.
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    return persons.stream().collect(Collectors.toMap(Person::id, this::convertPersonToString, (a, b) -> a));
  }

  // есть ли совпадающие в двух коллекциях персоны?
  // Конвертируем persons2 в Set, чтобы проверять наличие элементов в нем за O(1).
  // Используем anyMatch для проверки наличия какого-либо из элементов persons1 в persons2Set.
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    Set<Person> persons2Set = new HashSet<>(persons2);
    return persons1.stream().anyMatch(persons2Set::contains);
  }

  // Посчитать число четных чисел
  // Переменная count лишняя, можно использовать .count() из Stream API
  public long countEven(Stream<Integer> numbers) {
    return numbers.filter(num -> num % 2 == 0).count();
  }

  // Загадка - объясните почему assert тут всегда верен
  // Пояснение в чем соль - мы перетасовали числа, обернули в HashSet, а toString() у него вернул их в сортированном порядке
  void listVsSet() {
    List<Integer> integers = IntStream.rangeClosed(1, 10000).boxed().collect(Collectors.toList());
    List<Integer> snapshot = new ArrayList<>(integers);
    Collections.shuffle(integers);
    Set<Integer> set = new HashSet<>(integers);
    assert snapshot.toString().equals(set.toString());
  }

  /*
   * Чтобы понять, почему set.toString() возвращает числа в отсортированном порядке, нужно знать как устроен HashSet.
   * HashSet не гарантирует, что элементы будут храниться в порядке добавления или в сортированном порядке.
   * Несмотря на это, в этом примере порядок сохраняется.
   * Порядок элементов в HashSet зависят от хэшей элементов. В данном случае HashSet хранит числа от 1 до 10000,
   * и хэш-функция выдает хэши с меньшими значениями для меньших чисел, большие хэши для больших чисел.
   * Поэтому, хоть в HashSet порядок не гарантирован, внутренне числа хранятся в отсортированном виде.
   */
}
