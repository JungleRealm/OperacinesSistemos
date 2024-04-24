# OperacinesSistemos

Projekto dokumentas:
https://docs.google.com/document/d/1pGB-vz-ZjPa4dmjG2OiJROkxBb-tjP7kLo9eOjqDCvM/edit

Projekto eiga:
1. Sukuriamas begalinis ciklas.
2. Ieskomas E: flash drive atmintine.
3. Kol atmintine nera prijungta, tol programa nieko nevykdo.
4. Kai prijungiama atmintine, ieskomas sis failas "E:\\new\\input.txt".
5. Vykdomas metodas mount() kuris i Flash Drive klases atminti patalpina failo turini.
6. Patikrinama ar Real Memory (hardas) turi pakankamai atminties visam failui patalpinti. Jei ne, kreipiamasi i External Memory ir tikrinama ar External Memory turi pakankamai vietos failui patalpinti. Kitu atveju metama atminties klaida.
7. Programa nuskaito visa atminti ir suranda visus failu pavadinimus.
8. Vartotojas pasirenka koki faila vykdyti.
9. Patikrinama ar supervizorine atmintis turi pakankamai atminties pasirinktam failui isikelti. Jei ne, metama atminties klaida.
10. Failas perkeliamas i supervizorine atminti.
11. Patikrinama failo sintakse. Jei failas neatitinka reikalavimu, metama klaida.
12. Tikrinama ar Real Memory turi pakankamai atminties virtualiai masinai sukurti. Jei ne, metama klaida.
13. Sukuriama virtuali masina.
14. Viskas is supervizorines atminties perkeliama i virtualios masinos atminti atvirkstine tvarka (reverse order).
15. Vartotojas pasirenka ar nori vykdyti komandas zingsniniu rezimu ar ne.
16. Virtuali masina vykdo komandas kol pasibaigia failas, ivyksta interuptas arba randama HALT komanda.
17. Vykdymo metu PRTN komanda perkelia visa informacija kuri yra rezultatu steke i spausdintuvo atminti (Print klase).
18. Spausdintuvas atspausdina visa paduota informacija.
19. Programai baigus darba, Real Memory atmintis, External Memory atmintis, Flash Drive atmintis, Virtual Memory atmintis it Supervizorine atmintis yra isvalomos.
20. Visos reiksmes atstatomos i DEFAULT reiksmes.


Known things to improve:
1. Flag'ai.
2. Kartais vykdant daug programu is eiles, kazkur prisideda indeksas ir atspausdinama daugiau reiksmiu nei reikia.
3. Nera kuriamas log'as.
4. Channel Device klase tureti naudoti interuptus?
5. Interuptai gali buti patobulinti.
6. Igyvendinti Jump komandas.