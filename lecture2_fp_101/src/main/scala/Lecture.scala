import SlideshowUtil._
import japgolly.scalajs.react.ScalaComponent
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

object Lecture extends JSApp {

  import Enumeration._

  val overview = chapter(
    chapterSlide(
      <.h1("Functional Programming 101")
    ),

    slide(
      "What we will learn in this lecture",
      Enumeration(
        Item.stable(<.p("Introduction")),
        Item.fadeIn(<.p("Immutability")),
        Item.fadeIn(<.p("Algebraic Data Types")),
        Item.fadeIn(<.p("Pure Functions")),
        Item.fadeIn(<.p("Referential Transparency")),
        Item.fadeIn(<.p("Recursion")),
        Item.fadeIn(<.p("Composability"))
      )
    ),

    noHeaderSlide(
      <.h2("You have a question?"),
      <.h3("Ask it right away!")
    )
  )

  val introduction = chapter(
    chapterSlide(
      <.h2("Introduction")
    ),

    slide(
      "Definition",
      <.p("FP is a declarative programming paradigm which tries to structure code as expressions and declarations. Thus, we end up with pure functions, no side-effects and shared mutaple state.")
    ),

    slide(
      "Who is it coming from",
      <.p("The basic ideas stem from Lambda Calculus which was developed by Alonzo Church in the 1930s.")
    ),

    slide(
      "What we will discuss",
      <.p("We will concentrate on typed Functional programming fulfilling all properties."),
      <.br,
      Enumeration(
        Item.stable(<.p("Immutability")),
        Item.stable(<.p("Algebraic Data Types")),
        Item.stable(<.p("Pure Functions")),
        Item.stable(<.p("Referential Transparency")),
        Item.stable(<.p("Recursion")),
        Item.stable(<.p("Composability"))
      )
    ),

    slide(
      "What other people/languages do",
      <.p("Other languages/people might just choose a subset.")
    )
  )

  val immutability = chapter(
    chapterSlide(
      <.h2("Immutability")
    ),

    slide(
      "Immutability: definition",
      <.p("Data never changes.")
    ),

    slide(
      "Data never changes",
      <.h4("We already know it!"),
      <.br,
      code("""
        // you cannot reassign to `a`
        val a = 5



        // you cannot mutate fields in a clase class instance
        case class Person(name: String, age: Int)

        val gandalf = Person("Gandalf", 2019)
    
        gandalf.name = "Gandolf" // not allowed
      """)
    ),

    slide(
      "Data never change",
      <.h4("Once created your data stays the same until destruction")
    ),

    noHeaderSlide(
      <.h4("What's the benefit?"),
      <.br,
      Enumeration(
        Item.stable(<.p("state of your values known at all times")),
        Item.stable(<.p("no race-conditions in a concurrent scenario ")),
        Item.fadeIn(<.p("simplifies reasoning about values in your code"))
      )
    )
  )

  val adts = chapter(
    chapterSlide(
      <.h2("Algebraic Data Types")
    ),

    slide(
      "ADT: definition",
      <.p("The are compositions of types and resemble algebraic operations.")
    ),

    slide(
      "ADT: product types",
      <.h4("Types are grouped into specific classes"),
      <.br,
      code("""
        // exists a value or not
        sealed trait Option[A]
        case class Some[A](a: A) extends Option[A]
        case class None[A]()     extends Option[A]
      """)
    ),

    slide(
      "ADT: sum types",
      <.h4("A single class combining multiple types"),
      <.br,
      code("""
        case class Pair[A, B](a: A, b: B)
      """)
    ),

    slide(
      "ADT: mix and match",
      code("""
        // already know this construct
        // Person is a product type
        sealed trait Person

        // each class is a sum type
        case class Wizard(name: String, power: String) extends Person
        case class Elf(name: String, age: Int)         extends Person
        case class Dwarf(name: String, height: Int)    extends Person
      """)
    ),

    slide(
      "ADT",
      <.p("We already knew this constructs. Now we have a name for them.")
    )
  )

  val pureFunctions = chapter(
    chapterSlide(
      <.h2("Pure Functions")
    ),

    slide(
      "Pure Functions: definition",
      <.p(
        "A function which, for any given input, returns an output and returns the same output for the same input at any time. " +
        "Furthermore, it doesn't effect the \"real world\"."
      )
    ),

    slide(
      "Pure Functions: in a mathematical sense",
      <.p(
        ^.cls := "math",
        "$f : a \\rightarrow b$"
      )
    ),

    slide(
      "Pure Functions",
      code("""
        def plus(a: Int, b: Int): Int = a + b

        // works for every input and always returns the same output
        plus(1, 2) == 3
      """)
    ),

    slide(
      "Pure Functions: replace with value",
      code("""
        val plus = Map(
          ...,
          1 -> Map(
            ...
            2 -> 3
            ...
          )
          ...
        )

        plus(1)(2) == 3
      """)
    ),

    slide(
      "Impure Functions: exceptions",
      code("""
        def divide(a: Int, b: Int): Int = a / b

        // throws an ArithmeticExceptions which bypasses your call stack
        divide(1, 0) == ???
      """)
    ),

    slide(
      "Impure Functions: partial functions",
      code("""
        // partial function
        def question(q: String): Int = q match {
          case "answer to everything" => 42
        }

        // throws an Exceptions which bypasses your call stack
        question("is the sun shining") == ???
      """)
    ),

    slide(
      "Impure Functions: non-determinism",
      code("""
        def question(q: String): Int = Random.nextInt()

        question("what is the answer to everything") == 42
      """),
      codeFragment("""
        question("what is the answer to everything") == 136
      """),
      codeFragment("""
        question("what is the answer to everything") == 5310
      """)
    ),

    slide(
      "Impure Functions: depend on \"the real world\"",
      code("""
        // reads file from disk
        def readFile(path: String): Either[Exception, File] = ???
      """),
      codeFragment("""
        // it might return a file handle
        reaedFile("/usr/people.cvs") == Right(File(...))
      """),
      codeFragment("""
        // or fail, e.g. file is missing, not enough rights, ...
        reaedFile("/usr/people.cvs") == Left(FileNotFoundException)
      """) 
    ),

    slide(
      "Impure Functions: effect \"the real world\"",
      code("""
        // writes file to disk
        def writeFile(file: File): Either[Exception, Unit] = ???
      """),
      codeFragment("""
        // changes the state of your machine aka real world
        val file = new File(...)
        writeFile(file)
      """)
    ),

    slide(
      "Impure Functions: effect \"the real world\"",
      code("""
        // even logging is changing the world
        def log(msg: String): Unit = println(msg)

        log("hello world")
      """)
    ),

    slide(
      "Impure Functions: effect \"the real world\"",
      <.h4("Application Input/Output (IO) is not allowed. How to write useful programs then?"),
      <.br,
      <.h5(
        ^.cls := "fragment fade-in",
        "We will discuss that later!"
      )
    ),

    exerciseSlide(
      "Is this function pure?",
      code("""
        def multiply(a: Int, b: Int): Int = a * b
      """),
      codeFragment("""
        // yes
      """),
      <.br,
      code("""
        def concat(a: String, b: String): String = {
          println(a)
          a + b
        }
      """),
      codeFragment("""
        // no -> we mutate the OS by writing to System Out
      """)
    ),

    exerciseSlide(
      "Is this function pure?",
      code("""
        def whatNumber(a: Int): String = a > 100 match {
          case true => "large one"
        }
      """),
      codeFragment("""
        // no -> partial function, only handles the `true` case
      """),
      <.br,
      code("""
        def increase(a: Int): Int = {
          // current time in milliseconds
          val current = System.currentTimeMillis
          
          a + current
        }
      """),
      codeFragment("""
        // no -> result depends on the time we call the function
      """)
    ),

    noHeaderSlide(
      <.h4("What's the benefit?"),
      <.br,
      Enumeration(
        Item.stable(<.p("makes it easy to reason about code")),
        Item.fadeIn(<.p("separates business logic from real world interaction"))
      )
    )
  )

  val referentialTransparency = chapter(
    chapterSlide(
      <.h2("Referential Transparency")
    ),

    slide(
      "Referential Transparency: definition",
      <.p("An expression is referential transparent if you can replace it by its evaluation result without changing the programs behavior. " + 
          "Otherwise, it is referential opaque.")
    ),

    slide(
      "Referential Transparency",
      Enumeration(
        Item.stable(<.p("no side-effects")),
        Item.fadeIn(<.p("same output for the same input")),
        Item.fadeIn(<.p("execution order doesn't matter"))
      )
    ),

    slide(
      "Referential Transparency: pure functions",
      <.h4("All pure functions are referential transparent")
    ),

    slide(
      "Referential Opaque",
      <.h4("We already know what effectful and non-deterministic functions look like"),
      <.br,
      <.h4("But what is with the execution order?")
    ),

    slide(
      "Reftrencial Opaque: variables and execution order",
      code("""
        // defines a variable - a mutable (imperative) reference
        var a = 1

        a = a + 1
        a == 2

        a = a + 1
        a == 3
      """)
    ),

    noHeaderSlide(
      <.h4("What's the benefit?"),
      <.br,
      Enumeration(
        Item.stable(<.p("makes it easy to reason about code")),
        Item.fadeIn(<.p("separates business logic from real world interaction"))
      )
    )
  )

  val recursion = chapter(
    chapterSlide(
      <.h2("Recursion")
    ),

    slide(
      "Recursion: definition",
      <.p("Solving a problem where the solution depends on solutions to smaller instances of the same problem.")
    ),

    slide(
      "Recursion: types",
      Enumeration(
        Item.stable(<.p("single/multi recursion")),
        Item.fadeIn(<.p("direct/indirect recursion")),
        Item.fadeIn(<.p("structural/generative recursion"))
      )
    ),

    slide(
      "Recursion: single/multi direct",
      <.img(
        ^.alt := "Multi Single Recursion",
        ^.src := "./img/single_multi_rec.svg",
        ^.width := "50%"
      )
    ),

    slide(
      "Recursion: indirect",
      <.img(
        ^.alt := "Indirect Recursion",
        ^.src := "./img/indirect_rec.svg",
        ^.width := "20%"
      )
    ),

    noHeaderSlide(
      <.h3("Data Types")
    ),

    slide(
      "Recursion: data types",
      <.p("Definition of the data structures depends on itself.")
    ),

    slide(
      "Recursion: direct single",
      code("""
        // linked list of Ints
        sealed trait IntList

        // a single list element with its Int value and the remaining list
        // this class of IntList is defined by using IntList (tail)
        case class Cons(head: Int, tail: IntList) extends IntList

        // end of the list
        case object Nil extends IntList
      """),
      codeFragment("""

        val list = Cons(0, Cons(1, Cons(2, Nil)))
      """)
    ),

    slide(
      "Recursion: direct single",
      <.img(
        ^.alt := "Linked List",
        ^.src := "./img/list.svg"
      )
    ),

    noHeaderSlide(
      <.h3("We have to write list types for every data types?"),
      <.br,
      <.h4(
        ^.cls := "fragment fade-in",
        "Fortunately no, we can use type parameters!"
      )
    ),

    slide(
      "Type Parameter: ADT",
      code("""
        sealed trait List[A]
        //                ^
        //                '
        //           type parameter
      """),
      codeFragment("""
        case class Cons[A](head: A, tail: List[A]) extends List[A]
        //              ^        ^             ^                ^
        //              '        '----         '----------------|
        //         type parameter    '                          |
        //                          fixes type of our value     |
        //                                                      '
        //                                    fixes type of remaing/whole list 

        case class Nil[A]() extends List[A]
      """)
    ),

    slide(
      "Type Parameter: ADT",
      code("""
        val intList  = Cons[Int](0, Cons[Int](1, Cons[Int](2, Nil[Int]())))
        val charList = Cons[Char]('a', Cons[Char]('b', Nil[Char]()))
      """),
      codeFragment("""
        // Scala can infer `A` by looking at the values

        val intList  = Cons(0, Cons(1, Cons(2, Nil())))
        // Scala knows that `0: Int`
        //   '- List[A] ~ List[Int]

        val charList = Cons('a', Cons('b', Nil()))
        // Scala knows that `'a': Char`
        //   '- List[A] ~ List[Char]
      """)
    ),

    slide(
      "Type Parameter: ADT",
      Enumeration(
        Item.stable(<.p("also called generics")),
        Item.fadeIn(<.p("can be fixed by hand: ", <.code("Cons[Int](0, Nil[Int]): List[Int]"))),
        Item.fadeIn(<.p("or inferred by Scala: ", <.code("Cons(0, Nil): List[Int]")))
      )
    ),

    exerciseSlide(
      "Let's Code",
      bashCode("""
        sbt> project fp101-exercises
        sbt> test:testOnly RecursiveDataSpec
      """)
    ),

    noHeaderSlide(
      <.h3("Now we have recursive data structures"),
      <.br,
      <.h4("But how do we process them?")
    ),

    noHeaderSlide(
      <.h3("Functions")
    ),

    slide(
      "Recursion: functions",
      <.p("Functions which call themselves.")
    ),

    slide(
      "Recursion: example",
      code("""
        //factorial
        def fact(n: Int): Int = n match {
          case 1 => 1
          case _ => n * fact(n - 1)
        }
      """),
      codeFragment("""
          fact(3)
        //  '- 3 * fact(2)
        //           '- 2 * fact(1)
        //                    '- 1
      """),
      codeFragment("""
        fact(3) == 3 * fact(2)
                == 3 * 2 * fact(1)
                == 3 * 2 * 1
                == 6
      """)
    ),

    slide(
      "Recursion: direct single",
      code("""
        def length(list: List[Int]): Int = list match {
      """),
      codeFragment("""
        // final state
          case Nil()         => 0
      """),
      codeFragment("""
        // single direct recusive step (length calls itself)
          case Cons(_, tail) => 1 + length(tail)
        }
      """)
    ),

    slide(
      "Recursion: direct single",
      code("""
        val list = Cons(1, Cons(2, Nil()))

        length(list) == 1 + length(Cons(2, Nil()))
                     == 1 + 1 + length(Nil())
                     == 1 + 1 + 0
                     == 2
      """)
    ),

    slide(
      "Recursion: direct multi",
      code("""
        /* Tree: either a leaf with a value or a node consisting of a 
         *        left and right tree 
         */

        def size(tree: Tree[Int]): Int = tree match {
      """),
      codeFragment("""
        // final state
          case Leaf(_)           => 1
      """),
      codeFragment("""
        // multiple direct recusive steps (branches into two recursice calls)
          case Node(left, right) => size(left) + size(right)
        }
      """)
    ),

    slide(
      "Recursion: direct multi",
      code("""
        val tree = Node(Node(Leaf(1), Leaf(2)), Leaf(3))

        size(tree) == size(Node(Leaf(1), Leaf(2))) + size(Leaf(3))
                   == size(Leaf(1)) + size(Leaf(2)) + 1
                   == 1 + 1 + 1
                   == 3
      """)
    ),

    slide(
      "Recursion: indirect",
      code("""
        def even(n: Int): Boolean = 
          if (n == 0) true
          else        !odd(n - 1)

        def odd(n: Int): Boolean =
          if (n == 0) true
          else        !even(n - 1)
      """)
    ),

    slide(
      "Recursion: indirect",
      code("""
        even(5) == !odd(4)
                == even(3)
                == !odd(2)
                == even(1)
                == !odd(0)
                == false
      """)
    ),
 
    slide(
      "Structural Recursion",
      <.p("When you consume a (recursive) data structure which gets smaller with every step, " + 
          "e.g. length of a list. This kind of recursion is guaranteed$^1$ terminate.")
    ),

    slide(
      "Generative Recursion",
      <.p("Generates a new data structure from its input and continues to work on it. This kind of recursion isn't guaranteed to terminate. " + 
          "Examples are sorting algorithm like quicksort.")
    ),

    exerciseSlide(
      "What kind of recursion is it?",
      code("""
        def plus(n: Int, m: Int): Int = n match {
          case 0 => m
          case _ => 1 + plus(n - 1, m)
        }
      """),
      codeFragment("""
        // structural direct single
      """)
    ),

    exerciseSlide(
      "What kind of recursion is it?",
      code("""
        def produce(n: Int): List[Int] = n match {
          case 0 => Nil()
          case _ => Cons(n, produce(n))
        }
      """),
      codeFragment("""
        // generative direct single
      """)
    ),

    exerciseSlide(
      "What kind of recursion is it?",
      code("""
        def parseH(str: List[Char]): Boolean = str match {
          case Cons('h', tail) => parseE(tail)
          case _               => false
        }
        def parseE(str: List[Char]): Boolean = str match {
          case Cons('e', tail) => parseY(tail)
          case _               => false
        }
        def parseY(str: List[Char]): Boolean = str match {
          case Cons('y', Nil()) => true
          case Cons('y', tail)  => parseH(tail)
          case _                => false
        }
      """),
      codeFragment("""
        // structural indirect multi
      """)
    ),

    slide(
      "What are possible problems",
      Enumeration(
        Item.stable(<.p("How to fix types of Generics?")),
        Item.fadeIn(<.p("What is the impact of recursion on our hardware?"))
      )
    ),

    slide(
      "Type Parameter: functions",
      <.h4("Again, do we need to write a function for every `A` in a Generic?"),
      <.br,
      <.h5(
        ^.cls := "fragment fade-in",
        "No! type parameters to the rescue."
      )
    ),

    slide(
      "Type Parameters: functions",
      code("""
        def length[A](list: List[A]): Int = list match {
        //         ^             ^
        //         '             '---------
        //    type parameter              '
        //                         fixes list type `A`
       
          case Nil()         => 0
          case Cons(_, tail) => 1 + length[A](list)
        }
      """)
    ),

    slide(
      "Type Parameters: functions",
      code("""
        length[Int](Cons(1, Cons(2, Nil()))) == 2

        // or we rely on inference again

        length(Cons(1, Cons(2, Nil()))) == 2
        // Scala knows that `1: Int`
        //   '- List[A] ~ List[Int]
        //        '- length[A] ~ length[Int] 
      """)
    ),

    slide(
      "Recursion: hardware impact",
      <.h4("How does multiple recursive calls reflect on the hardware?")
    ),

    slide(
      "Recursion: call stack",
      code("""
        length(Cons(0, Cons(1, Nil())))
        // '- 1 + length(Cons(1, Nil()))
        //           '- 1 + length(Nil())
        //                     '- 0
      """)
    ),

    slide(
      "Recursion: call stack",
      <.img(
        ^.alt   := "Programm Stack",
        ^.width := "45%",
        ^.src   := "./img/stack.svg"
      )
    ),

    slide(
      "Recursion: call stack",
      <.h4("But what happens if the list is reaaaaally long?"),
      <.br,
      <.h5(
        ^.cls := "fragment fade-in",
        "Your programm will run out of memory (stack overflow)"
      )
    ),

    slide(
      "Recursion: tail recursion",
      <.p("One way to solve that is to make the function tail-recursive. This means, the function must have single, direct " + 
          "recursion and the last expression is the recursive call.")
    ),

    slide(
      "Recursion: tail recursion",
      code("""
        def lengthSafe[A](list: List[A]): Int = list {
          def loop(remaining: List[A], agg: Int): Int = remaining match {
            case Nil()         => agg
            case Cons(_, tail) => loop(tail, agg + 1)
            //                      ^
            //                      '
            //                 last expression
          }

          loop(list, 0)
        }

        lengthSafe(Cons(0, Cons(1, Nil()))) == 2
      """)
    ),

    slide(
      "Recursion: tail recursion",
      <.p("Scala optimizes this function to an imperative loop. Therefore, the stack does not grow.")
    ),

    slide(
      "Recursion: tail recursion",
      code("""
        def lengthSafe[A](list: List[A]): Int = list {

          // Scala now checks of this function is tail recursive
          @tailrec
          def loop(remaining: List[A], agg: Int): Int = ???

          loop(list, 0)
        }
      """)
    ),

    exerciseSlide(
      "Is this function stack-safe?",
      code("""
        def fib(n: Int): Int = n match {
          case 1 | 2 => 1
          case _     => fib(n - 1) + fib(n - 2)
        }
      """),
      codeFragment("""
        /* no, last expression is the `+` operator and we 
         * create two recursion branches 
         */
      """)
    ),

    exerciseSlide(
      "Is this function stack-safe?",
      code("""
        def last[A](list: List[A]): List[A] = list match {
          case el@ Cons(_, Nil()) => el
          case Cons(_, tail)      => last(tail)
        }
      """),
      codeFragment("""
        // yes, last expression is `last`
      """)
    ),

    exerciseSlide(
      "Is this function stack-safe?",
      code("""
        def size[A](tree: Tree[A]): Int = tree match {
          case Leaf(_)           => 1
          case Node(left, right) => size(left) + size(right)
        }
      """),
      codeFragment("""
        /* no, again the last expression is the `+` operator 
         * and we create two recursion branches
         */
      """)
    ),

    exerciseSlide(
      "Let's Code",
      bashCode("""
        sbt> project fp101-exercises
        sbt> test:testOnly RecursiveFunctionsSpec
      """)
    ),

    slide(
      "But what with all the other recursion types?",
      <.p(
        "There is no tool Scala or the JVM can provide us here. We have to rely on a technique called ",
        <.strong("Trampolining"),
        ". We won't discuss that in this course."
      )
    ),

    noHeaderSlide(
      <.h4("What's the benefit?"),
      <.br,
      Enumeration(
        Item.stable(<.p("represent collections")),
        Item.fadeIn(<.p("solve complex problems with divide & conquer"))
      )
    )
  )

  val composition = chapter(
    chapterSlide(
      <.h2("Composition")
    ),

    slide(
      "Composition: definition",
      <.p("Build complex programs out of simple ones.")
    ),

    slide(
      "Composition: math",
      <.p(
        ^.cls := "math",
       """
         \[\begin{aligned}
	   f : b \rightarrow c \\
           g : a \rightarrow b \\
           \newline
           f . g : a \rightarrow c
         \end{aligned} \]
       """
      )
    ),

    slide(
      "Composition: code",
      code("""
        def compose[A, B, C](f: B => C)(g: A => B): A => C = 
          a => f(g(a))
      """),
      codeFragment("""
        def double(a: Int): Int = a * 2
        def show(a: Int): String = a.toString

        val complex = compose(show)(double)

        complex(2) == "4"
      """)
    ),

    slide(
      "Composition: Scala",
      code("""
        // already built-in
        (show _).compose(double)
        //  ^
        //  '- transforming method to a function object
      """),
      codeFragment("""
        // or work directly with function objects
        val show: Int => String = _.toString
        val double: Int => Int  = _ * 2

        show.compose(double)
      """)
    ),

    slide(
      "Composition: Scala",
      code("""
        double.andThen(show) == show.compose(double)
      """)
    ),

    slide(
      "Composition: data structures",
      code("""
        // consider the following
        def replicate[A](n: Int, a: A): List[A] = 
          if (n == 0) Nil() else Cons(a, replicate(n - 1, a))


        def show[A](a: A): String = a.toString
      """),
      <.br,
      <.p(
        ^.cls := "fragment fade-in",
        "How to compose `replicate` and `show`?"
      )
    ),

    slide(
      "Composition: data structures",
      code("""
        // we already know map (exercises)
        def map[A, B](as: List[A])(f: A => B): List[B]

        map(as)(a => replicate(n, a)) // will not work        
      """),
      codeFragment("""
        f: A => B != replicate: A => List[B]
      """)
    ),

    slide(
      "Composition: data structures",
      code("""
        // list[list[a]] -> list[a]
        def flatMap[A, B](as: List[A])(f: A => List[B]): List[B] = as match {
      """),
      codeFragment("""
        // empty case
          case Nil()         => Nil()
      """),
      codeFragment("""
        // recursive step
          case Cons(a, tail) => 
            val bs = f(a)
            
            combine(bs, flatMap(tail)(f))
        }
      """)
    ),

    slide(
      "Composition: data structures",
      code("""
        val n = 2

        val complex: List[A] => List[A] = 
          as => map(flatMap(as)(replicate(n, _)))(show)


        val list = Cons(0, Nil())
        
        complex(list) == Cons(Cons("0", Cons("0", Nil())), Nil())
      """)
    ),

    slide(
      "Composition: for-comprehension",
      code("""
        // make map and flatMap methods of List
        sealed trait List[A] {
          
          def map[B](f: A => B): List[B]
          def flatMap[B](f: A => List[B]): List[B]
        }
      """),
      codeFragment("""
        // Scala lets you use for-comprehension
        val complexFor: List[Int] => List[Int] = as => 
          for {
            a   <- as
            rep <- replicate(n, a)
          } yield show(rep)

        complexFor(list) == complex(list)
      """)
    ),

    slide(
      "Composition: for-comprehension",
      code("""
        // comes in handy later on
        for {
          a <- f(in)
          b <- g(a)
          ...
          z <- h(???)
        } yield doSomething(z)
      """)
    ),

    exerciseSlide(
      "Let's Code",
      bashCode("""
        sbt> project fp101-exercises
        sbt> test:testOnly CompositionsSpec
      """)
    ),

    noHeaderSlide(
      <.h4("What's the benefit?"),
      <.br,
      Enumeration(
        Item.stable(<.p("solve complex problems with divide & conquer"))
      )
    )
  )

  val summary = chapter(
    chapterSlide(
      <.h2("Summary")
    ),

    slide(
      "Immutability",
      <.h4("Data never changes")
    ),

    slide(
      "Algebraic Data Structures",
      <.h4("Composition of types")
    ),

    slide(
      "Pure Functions",
      <.p(
        ^.cls := "math",
        "$f : a \\rightarrow b$"
      )
    ),

    slide(
      "Referential Transparency",
      <.h4("Expression can be replaced by their evaluation result")
    ),

    slide(
      "Recursion",
      <.h4("Solving a problem where the solution depends on solutions to smaller instances of the same problem")
    ),

    slide(
      "Composition",
      <.h4("Creating complex problems out of simpler ones")
    ),

    noHeaderSlide(
      <.h2("Next Topic"),
      <.br,
      <.h3("Scala Standard Library")
    )
  )

  val Show = ScalaComponent
    .builder[Unit]("Slideshow")
    .renderStatic(
      <.div(
        ^.cls := "reveal",
        <.div(
          ^.cls := "slides",
          overview,
          introduction,
          immutability,
          adts,
          pureFunctions,
          referentialTransparency,
          recursion,
          composition,
          summary
        )
      )
    )
    .build

  @JSExport
  override def main(): Unit = {
    Show().renderIntoDOM(dom.document.body)
  }
}
