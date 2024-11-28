using System.ComponentModel.DataAnnotations;

public class ApplyForLoanRequest
{
    [Required(ErrorMessage = "Loan amount is required.")]
    [Range(300, double.MaxValue, ErrorMessage = "Loan amount must be greater than 300.")]
    public double LoanAmount { get; set; }

    [Required(ErrorMessage = "Term is required.")]
    [Range(1, int.MaxValue, ErrorMessage = "Term must be at least 1 month.")]
    public int Term { get; set; }
}