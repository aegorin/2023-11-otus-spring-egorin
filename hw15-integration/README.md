## Домашнее задание №15

Реализовать обработку доменной сущности через каналы Spring Integration.

Программа формирует отчёт по сотрудникам за предыдущий год с поквартальной разбивкой по каждому сотруднику.
В итоговом отчёте сотрудники выводятся в отсортированном виде продуктивного к менее продуктивному.

Пример выполнения shell-команды **report**: 
```
shell:> report John Alice
Report:
    Person: Alice
        01.01.2023 - 31.03.2023:   124
        01.04.2023 - 30.06.2023:    73
        01.07.2023 - 30.09.2023:   144
        01.10.2023 - 31.12.2023:   106
                          Total:   447
    Person: John
        01.01.2023 - 31.03.2023:    36
        01.04.2023 - 30.06.2023:    83
        01.07.2023 - 30.09.2023:    26
        01.10.2023 - 31.12.2023:   149
                          Total:   294
```