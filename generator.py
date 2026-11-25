"""
--------------------------------------------------------------------------------
generator.py

(Interim)

Implementation of an automated test-case generator based on simple JSON
models.
--------------------------------------------------------------------------------
"""

# Python standard library imports
from enum import Enum
import io
import os
import json
import sys

# PyPI imports
import pandas as pd
from sympy import symbols, reduce_inequalities, Eq, solve_univariate_inequality


class ArgType(str, Enum):
    '''
    LongStr refers to a "long-as-a-string" data type exclusively for the
    DateHelper methods, to indicate the data type semantically should be a
    long, but is actually a String.
    '''

    LLONG = "long"
    LONG = "Long"
    LONGSTR = "longStr"
    INT = "int"
    DATETIMESTR = "DateTimeString"


def write_tests(out_filename, function_name, frames):

    data = [{arg_name: case} for arg_name, case in frames]

    with open(out_filename, "w") as file:
        json.dump(data, file, indent=4)


def combinatorial_solver(data):

    print(data)

    frames = []

    for arg_name, arg_type in data.items():

        test_cases = []

        match arg_type:

            case ArgType.LONG:
                test_cases = [
                    -1,
                    0,
                    1720875040000,
                    # 9223372036854775807,
                    # -9223372036854775807
                ]

            case ArgType.LLONG:
                test_cases = [
                    -1,
                    0,
                    1720875040000,
                    # 9223372036854775807,
                    # -9223372036854775807
                ]


            case ArgType.LONGSTR:
                test_cases = [
                    "-1",
                    "0",
                    "1720875040000",
                    "",
                    # 9223372036854775807,
                    # -9223372036854775807
                ]


        for case in test_cases:
            frame = (arg_name, case)
            frames.append(frame)

    return frames


def generate_combinatorial(target: str):

    with open(target, "r", encoding="utf-8") as f:
        model = json.load(f)

    for function_name, function_data in model.items():
        args = function_data['argtypes']
        partitioned_cases = combinatorial_solver(args)
        write_tests(
            f"src/test/resources/{function_name}-combinatorial.json",
            function_name,
            partitioned_cases
        )


def main(*args):

    print("BEGIN test frame generation for DateHelper")

    print("BEGIN COMBINATORIAL test generation for DateHelper")

    generate_combinatorial("./genresources/datehelper-textmodel.json")

    print("END COMBINATORIAL test generation for DateHelper")

    print("Test case generation for DateHelper completed.")


if __name__ == "__main__":
    main()
