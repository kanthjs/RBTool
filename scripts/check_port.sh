#!/bin/sh
# Regression check: run every case suite on the PBTools port (reference/pbtools-port) (R 4.5.2) and compare with reference/original-outputs.
# Reference outputs are regenerated only with REGEN=1 (needs R 2.15.1 at C:\RPackages).
R45="/c/Program Files/R/R-4.5.2/bin/Rscript.exe"; R15="/c/RPackages/R-2.15.1/bin/i386/Rscript.exe"
cd "$(dirname "$0")/.." || exit 1
"$R45" -e 'install.packages("reference/pbtools-port", repos = NULL, type = "source")' >/dev/null 2>&1
tmp=$(mktemp -d)
for s in ssa:ssa_cases designs:designs_cases ms:ms_cases gxe:gxe_cases; do
  d=${s%%:*}; f=${s##*:}
  [ -n "$REGEN" ] && "$R15" scripts/$f.R . reference/original-outputs/$d original >/dev/null 2>&1
  "$R45" scripts/$f.R . "$tmp/$d" port >/dev/null 2>&1
  echo "== $d"; "$R45" scripts/compare_outputs.R reference/original-outputs/$d "$tmp/$d"
done
