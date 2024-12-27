using Microsoft.AspNetCore.Mvc;
using Microsoft.AspNetCore.Mvc.Filters;

namespace Loan.API.Filters;

public class ValidationFilter : IActionFilter
{
    public void OnActionExecuting(ActionExecutingContext context)
    {
        if (context.ModelState.IsValid) return;

        var errors = context.ModelState
            .Where(entry => entry.Value.Errors.Any())
            .ToDictionary(
                entry => entry.Key,
                entry => entry.Value.Errors.First().ErrorMessage
            );


        context.Result = new BadRequestObjectResult(new
        {
            message = "Invalid request body.",
            status_code = 400,
            errors
        });
    }

    public void OnActionExecuted(ActionExecutedContext context)
    {
    }
}