import validator from "validator";


export default class Engineer {
    constructor(name, tenure, nationality) {
        if (!name || !tenure || !nationality) {
            throw new Error("Fields must be filled")
        }
        if (!validator.isNumeric(tenure)) {
            throw new Error("Tenure must be numeric")
        } else if (validator.isNumeric(nationality)) {
            throw new Error("Nationality must be a text")
        } else if (validator.isNumeric(name)) {
            throw new Error("Name must be text")
        }
        this.name = name;
        this.tenure = tenure;
        this.nationality = nationality;
    }
}


