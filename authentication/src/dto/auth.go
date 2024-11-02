package dto

type LoginRequest struct {
	Email    string `json:"email" validate:"required"`
	Password string `json:"password" validate:"required"`
}

type RegisterRequest struct {
	Name         string `json:"name" validate:"required"`
	Username     string `json:"username" validate:"required"`
	Password     string `json:"password" validate:"required"`
	Email        string `json:"email" validate:"required,email"`
	AnnualIncome int    `json:"annual_income" validate:"required,number,gte=0"`
	Debt         int    `json:"debt" validate:"required,number,gte=0"`
	AssetsValue  int    `json:"assets_value" validate:"required,number,gte=0"`
}
