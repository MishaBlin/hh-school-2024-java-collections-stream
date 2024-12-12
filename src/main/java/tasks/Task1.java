package tasks;

import common.Person;
import common.PersonService;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/*
Задача 1
Метод на входе принимает List<Integer> id людей, ходит за ними в сервис
(он выдает несортированный Set<Person>, внутренняя работа сервиса неизвестна)
нужно их отсортировать в том же порядке, что и переданные id.
Оценить асимптотику работы
 */
public class Task1 {

  private final PersonService personService;

  public Task1(PersonService personService) {
    this.personService = personService;
  }

  /*
   * Решение использует Map для сохранения исходного порядка personIds.
   * Далее заполняем массив orderedPersons.
   * Асимптотика алгоритма - O(n), где n - количество элементов в списке personIds.
   */
  public List<Person> findOrderedPersons(List<Integer> personIds) {
    Set<Person> persons = personService.findPersons(personIds);

    Map<Integer, Integer> idToPosition = IntStream.range(0, personIds.size())
        .boxed()
        .collect(Collectors.toMap(personIds::get, Function.identity()));

    Person[] orderedPersons = new Person[persons.size()];
    persons.forEach(person -> orderedPersons[idToPosition.get(person.id())] = person);

    return Arrays.asList(orderedPersons);
  }
}
