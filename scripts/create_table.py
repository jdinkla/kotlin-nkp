import pandas as pd
import argparse

parser = argparse.ArgumentParser()
parser.add_argument("json_file", help="the file to display")
args = parser.parse_args()

df = pd.read_json(args.json_file)
df.fillna(0, inplace=True)
for column in df.columns:
    if pd.api.types.is_float_dtype(df[column]):
        df[column] = df[column].astype('Int64')
print(df)
