# 🎓 Platformă E-Learning - Etapa I

> **Proiect PAO 2026**
> Sistem de gestionare a cursurilor, materialelor și performanței studenților.

---

## 🛠️ 1. Definirea Sistemului

### 📋 1.1 Lista Acțiuni / Interogări
Sistemul implementat permite următoarele 10 operațiuni fundamentale:

1. **Înregistrare Student**: Adăugarea unui nou student în sistem (stocat în `List`).
2. **Înregistrare Instructor**: Adăugarea unui profesor cu specializarea aferentă.
3. **Creare Curs**: Definirea unui curs nou (titlu și ID unic).
4. **Adăugare Lecție Video**: Atașarea unui material de tip video la un curs existent.
5. **Adăugare Quiz**: Atașarea unui test de evaluare (moștenește clasa `Material`).
6. **Înregistrare Scor Quiz**: Salvarea unui rezultat imutabil (`ScorFinal`) în catalog.
7. **Căutare Curs după Titlu**: Localizarea unui curs cu gestionarea excepției `ResursaNegasitaException`.
8. **Listare Utilizatori**: Afișarea polimorfică a tuturor membrilor (Student/Instructor).
9. **Listare Cursuri Sortate**: Afișarea cursurilor în ordine alfabetică folosind `Comparable`.
10. **Ștergere Curs**: Eliminarea unui curs și a scorurilor asociate din `Map`.

> 💡 **Notă (`Main.java`)**: Clasa principală include o etapă de **populare inițială** cu date pre-existente, iar fiecare dintre cele 10 acțiuni de mai sus este urmată de o **verificare în consolă** pentru a demonstra modificarea stării sistemului în memorie.

---

### 🏗️ 1.2 Obiecte din Domeniu (Modele)
Am implementat următoarele tipuri de obiecte pentru a respecta principiile OOP:

* **`Utilizator`** *(Abstract)*: Clasa de bază cu atribute comune (ID, Nume).
* **`MembruPlatforma`** *(Abstract)*: Primul nivel de moștenire.
* **`Student`** & **`Instructor`**: Al doilea nivel de moștenire (Nivel 2).
* **`Curs`**: Implementează `Comparable<Curs>` pentru sortare.
* **`Material`** *(Abstract)*: Clasa de bază pentru conținut educațional.
* **`Quiz`** & **`LectieVideo`**: Specializări ale materialelor.
* **`ScorFinal`**: Clasă **imutabilă** pentru înregistrarea rezultatelor.

---

## 📂 Structura Proiectului
Codul este organizat în sub-pachete logice conform cerințelor:

```text
src/com/pao/proiect/elearning/
├── model/      # Entitățile OOP și ierarhiile de clase
├── service/    # Clasele de tip Singleton (Logica Business)
├── exception/  # Excepțiile custom (Checked & Unchecked)
└── Main.java   # Clasa de test care rulează cele 10 acțiuni