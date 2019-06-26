from openpyxl import load_workbook
wb = load_workbook("transction.xlsx")
sheet = wb.get_sheet_by_name("Sheet1")
for i in range(1,2165):
    print("("+str(sheet[i][0].value)+","+ str(sheet[i][1].value)+','+ str(sheet[i][2].value) + ','+
          '\"' + str(sheet[i][3].value) + '\"' + ','+ str(sheet[i][4].value)+','+ str(sheet[i][5].value)+','
         + '\"' + str(sheet[i][6].value)+'\"'+")"+",")
