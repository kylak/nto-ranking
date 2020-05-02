
public enum SyntacticRoles {

    // The defaut format (following one) is the CNTR's provided format (cf. the first url below).

    // From p.57-58 of https://greekcntr.org/downloads/project.pdf .

    N, // For "Noun".
    A, // For "Adjective".
    E, // For "dEterminer".
    R, // For "pRonoun".
    V, // For "Verb".
    I, // For "Interjection".
    P, // For "Preposition".
    D, // For "aDverb".
    C, // For "Conjunction".
    T; // For "parTicle".

    enum inBibleHub {   // From https://saintebible.com/abbrev.htm , put here in uppercase.

        // Called "Part of Speech" :

        V,    // For "Verb".
        N,    // For "Noun".
        ADV,  // For "Adverb".
        ADJ,  // For "Adjective".
        ART,  // For "Article".
        DPRO, // For "Demonstrative Pronoun".
        IPRO, // For "Interrogative / Indefinite".

        // Pronoun
        PPRO,   // For "Personal / Possessive".
        RECPRO, // For "Reciprocal Pronoun".
        RELPRO, // For "Relative Pronoun".
        REFPRO, // For "Reflexive Pronoun".

        PREP,  // For "Preposition".
        CONJ,  // For "Conjunction".
        I,     // For "Interjection".
        PRTCL, // For "Particle".
        HEB,   // For "Hébrey Word".
        ARAM,  // For "Aramaic Word".

    }

    /*
    enum inVerite {    // From https://github.com/verite/verite/blob/master/morph/morph.html .

        ADV, // For "Adverbe".
        ARAM,  // For "Araméen translittéré".
        C, // For "Pronom Réciproque".
        COND, // For "Conditionnel".
        CONJ, // For "Conjonction".
        CONJ-N, // For "Conjonction négatif".
        D, // "Pronom Démonstratif".
        F, // For "Pronom Réfléchi".
        HEB, // For "Hébreu translittéré".
        I, // For "Pronom Interrogatif".
        INJ, // For ""Interjection.
        K, // For "Pronom Corrélatif".
        N, // For "Noun".
        N-Li, // For "Lettre".
        N-OI, // For "Nom ou Autre".
        N-PRI // For "Nom Propre".
        P, // For "Pronom Personnel".
        PREP, // For "Préposition".
        PRT, // For "Particule".

        // There's more than what I expected : to continue for the one who need it.
        
    }
    */

}
