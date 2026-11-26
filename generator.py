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
from itertools import product
import os
import json
import sys

# PyPI imports
import pandas as pd
from sympy import symbols, reduce_inequalities, Eq, solve_univariate_inequality


# possible long values?
# -9223372036854775807


class ArgType(str, Enum):
    '''
    As stated in the report, these are custom types that are meant to reflect
    both the actual Java type (long, Long, String, DateFormats) as well as the
    expected type or format of the value in order to select the appriopriate
    set of values (category blocks) for each argument. This applies to both
    generating combinatorial test cases and category-partition test cases.

    The reason for this is the Java values alone do not accurately indicate
    what are expected input types in DateHelper. For example, a String can be
    partitioned into three blocks: an empty String, a String with a single
    element, and a String with more than one element. These blocks, however,
    are completely useless for DateHelper; we are more interested in the
    behaviour based on whether the string is formated correctly or not.
    Without special types we would end up using these naive String blocks
    where no inputs are valid.

    -   DATETIMESTR is a String specifically expected to be in a DateTimeFormat
        i.e. 20/06/2011
    -   LONGSTR refers to a "long-as-a-string" data type exclusively for the
        DateHelper methods, to indicate the data type semantically should be a
        long, but is actually a String.
    -   INT is Java's basic int type, used for Quadratic.java only.
    '''
    DATETIMESTR = "DateTimeString"
    DATETIMESTRFORMAT = "DateTimeStringAsFormat"
    DATEFORMATS = "DateFormats"
    LLONG = "long"
    LONG = "Long"
    LONGSTR = "longStr"
    INT = "int"


def write_tests(out_filename, frames):

    with open(out_filename, "w") as file:
        json.dump(frames, file, indent=4)


def combinatorial_solver(data):

    frames = []
    possible_vals = []
    arg_order = []

    for arg_name, arg_type in data.items():

        test_cases = []
        arg_order.append(arg_name)

        match arg_type:

            case ArgType.LONG:
                test_cases = [
                    -1,
                    0,
                    1720875040000,
                ]

            case ArgType.LLONG:
                test_cases = [
                    -1,
                    0,
                    1720875040000,
                ]

            case ArgType.LONGSTR:
                test_cases = [
                    "-1",
                    "0",
                    "1720875040000",
                    # "",
                ]

            case ArgType.DATEFORMATS:
                test_cases = [
                    "D_DDMMyy",
                    "D_YYYYMMDD_N"
                ]

            case ArgType.DATETIMESTR:
                test_cases = [
                    "20-10-25",
                ]

        possible_vals.append(test_cases)

    # pairwise_seen = set()
    arg_product = product(*possible_vals)

    for combination in arg_product:
        frames.append(dict(zip(arg_order, combination)))

    return frames


def generate_combinatorial(target: str):

    with open(target, "r", encoding="utf-8") as f:
        model = json.load(f)

    for function_name, function_data in model.items():
        args = function_data['argtypes']

        partitioned_cases = combinatorial_solver(args)
        write_tests(
            f"src/test/resources/{function_name}-combinatorial.json",
            partitioned_cases
        )


def main(*args):


    # TODO time generations
    print("BEGIN test frame generation for DateHelper")

    print("BEGIN COMBINATORIAL test generation for DateHelper")

    generate_combinatorial("./genresources/datehelper-textmodel.json")

    print("END COMBINATORIAL test generation for DateHelper")

    print("Test case generation for DateHelper completed.")


if __name__ == "__main__":
    main()
