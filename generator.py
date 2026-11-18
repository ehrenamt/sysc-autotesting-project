"""
--------------------------------------------------------------------------------
generator.py

(Interim)

Implementation of an automated test-case generator based on simple JSON
models.
--------------------------------------------------------------------------------
"""

# Python standard library imports
import io
import os
import json
import argparse

import sys

# PyPI imports
import pandas as pd
# from pandas.errors import ParserError
from sympy import symbols, reduce_inequalities, Eq, solve_univariate_inequality


def write_cases(in_filename, out_filename):

    data = []


    for a, b, c in [
        ("5", "0", "2"),
        ("0.5", "1", "2"),
        ("2", "4", "2"),
        ("-10", "13", "2"),
    ]:
        data.append({
            "a": a,
            "b": b,
            "c": c,
            "oracle": 10,
        })

    with open(out_filename, "w") as file:
        json.dump(data, file, indent=2)

def write_tests(out_filename, data):

    data = []


    for a, b, c in [
        ("5", "0", "2"),
        ("0.5", "1", "2"),
        ("2", "4", "2"),
        ("-10", "13", "2"),
    ]:
        data.append({
            "a": a,
            "b": b,
            "c": c,
            "oracle": 10,
        })

    with open(out_filename, "w") as file:
        json.dump(data, file, indent=2)


def partition_all(data):

    cases = []

    statements = data["cf_relations"]

    for statement in data["cf_relations"]:
        print(statement)

    return 1, 2, 3


def generate_categorical(target: str):

    # steps (no error checking)
    # read the target as json.
    # each top level KV pair is an entry function, with the key = function name.
    # so for each top level KV pair (function name):
    # we take its value and pass it into a "partition" function
    # we take the OUTPUT 
    # and pass the output to wrte_output
    # with {key}.json as the filename
    
    with open(target, "r", encoding="utf-8") as f:
        model = json.load(f)


    for function_name, function_data in model.items():
        partitioned_cases = partition_all(function_data)
        write_tests(f"{function_name}.json", partitioned_cases)

    # solveq = model["solveQuadratic"]

    # print(solveq)


# refactor main: first arg is an actual filepath string to the model (no error checking needed)
# and second remains the testing type
def main(*args):

    # temp
    a, b, c = symbols("a b c", real=True)
    lhs = b**2 - 4*a*c
    rhs = 0

    conditions = {
        ">": lhs > rhs,
        "<": lhs < rhs,
        "==": Eq(lhs, rhs),
    }

    # for op, condition in conditions.items():
    #     print(op, reduce_inequalities(condition, b))

    b = symbols("b", real=True)

    expr = b**2 - 4 * 1 * 1  # a=1, c=1
    solution = solve_univariate_inequality(expr > 0, b)

    print(solution)
    print(type(solution))

    target = args[0] if args else "quad"
    print(f"Target: {target}")

    test_type = args[1] if args else "combinatorial"

    print(f"Type: {test_type}")

    if target == "quad":

        match test_type:

            case "combinatorial":
                print("Generating combinatoria tests for Quadratic.Java")

            case "metamorphic":
                print("Generating metamorphic tests for Quadratic.Java")

            case "categorical":
                print("Generating category-partition tests for Quadratic.Java")
                generate_categorical("quadratic-textmodel.json")
                # write_cases(target, "categorical_suite.json")
            
            case _ :
                pass
            


if __name__ == "__main__":
    main(*sys.argv[1:])


# write_cases("src/test/resources/categorical_suite.json")

