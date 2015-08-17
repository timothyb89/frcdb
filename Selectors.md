# Introduction #

Selectors are a new method of performing advanced database-like queries on the raw dataset. Specifically, selectors are used for selecting targets for statistics, but are compatible with any entities stored in the database.

Selectors run in a sandboxed [Rhino](https://developer.mozilla.org/en-US/docs/Rhino) instance and are presently only available for access via the admin panel.

# Syntax #
Selectors use roughly the following syntax:
```
select("Type", {
    property: func(value),
    anotherProperty: func(anotherValue)
});
```

# Functions #

## Predefined ##
A number of custom functions are available:

| **Name**     | **Description**                        | **Property Type** | **Syntax**                           |
|:-------------|:---------------------------------------|:------------------|:-------------------------------------|
| db           | Queries the datastore directly.        | any Java native   | `db(condition, value)`               |
| lt           | Performs a less-than (<) test.         | Comparable        | `lt(value)`                          |
| gt           | Performs a greater-than (>) test.      | Comparable        | `gt(value)`                          |
| eq           | Performs an equality (==) test.        | Comparable        | `eq(value)`                          |
| startsWith   | Property starts with string `value`?   | String            | `startsWith(value)`                  |
| now          | Current time (Java)                    | n/a               | `now()`                              |
| offset       | Current time +/- `num` days            | n/a               | `offset(num)`, `offset(date, num)`   |
| date         | Specific date                          | n/a               | `date(year, month, day)`             |

## Custom Functions ##
Custom JavaScript functions may be used for more advanced queries. The primary requirement is that they return a lambda, and that the parameter to the enclosing function is the second argument in the comparison.

The inner function will be passed the various objects to be filtered, and then return true or false depending on whether or not the object matches the outer parameters.. For example, the `gt()` function could be reimplemented as:
```
gt = function(b) {
    return function(a) {
        return a.compareTo(b) > 0;
    }
}
```

### Function: `db(condition, value)` ###

  * `condition`: An Objectify-compatible condition, such as ">", "<", "=", etc
  * `value`: The value to compare candidates against.

These functions are executed separately from other functions and interact directly with the datastore to fetch the main dataset to select from. As the selection occurs in-memory, it's important to use the most restrictive database condition possible to reduce the object loading cost, as well as the memory and CPU consumption. Note that if no database filters are specified, every object of the given type will be loaded. While this is generally acceptable for Events and Games, loading every Team object is likely to be very costly.

Note that these filters have the same restrictions as normal datastore filters, and the property they're filtering against must have be indexed. Also note that multiple properties can not be filtered for unless a hybrid index exists already. Due to this limitation, most secondary queries will use one of the in-memory functions.

It's not currently possible to write a custom function to perform a similar task to `db()`. This functionality may be added in the future, however.

### Value Notes ###
Note that all queries expect a native Java value, which can be troublesome as Rhino occasionally has issues converting them automatically. While numbers and other primitives shouldn't pose a problem, some values, like JavaScript Date objects, should be avoided completely.

As the native Java Date object cannot be instantiated directly within the sandbox, the `now()`, `offset()`, and `date()` functions should be used instead. `new Date()` should not be used as it will instantiate a JavaScript Date object that is incompatible with the database's internal properties.

# Examples #

A selector that selects all ongoing events:
```
select("Game", {
    endDate: db(">", now()), // select all future-ending events from the db
    startDate: lt(now())     // filter only those that have already started
});
```