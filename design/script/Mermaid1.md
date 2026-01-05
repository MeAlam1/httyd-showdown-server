classDiagram
direction BT
class Coerce {
  - Coerce() 
  + getProperty(Object, String) Object
  + equalsLoose(Object, Object) boolean
  + toNumber(Object) double
  + toBool(Object) boolean
}
class DefaultOpcodes {
  - DefaultOpcodes() 
  + registerAll(OpcodeRegistry) OpcodeRegistry
}
class DefaultOpcodes {
  - DefaultOpcodes() 
  + createRegistry() OpcodeRegistry
  + registerAll(OpcodeRegistry) OpcodeRegistry
}
class EmitHandler {
  - EmitHandler() 
  - extractEventId(Map~String, Object~) String?
  + execute(ExecutionContext, Step) ExecutionResult
  - extractPayload(Map~String, Object~) Map~String, Object~?
}
class EmitOpcodeHandler {
  + EmitOpcodeHandler() 
  + execute(ExecutionContext, Step) ExecutionResult
}
class Event {
  + Event(String, Map~String, Object~) 
  + payload() Map~String, Object~
  + id() String
}
class ExecutionContext {
<<Interface>>
  + vars() Map~String, Object~
  + rng() Rng
  + emit(String, Map~String, Object~) void
}
class ExecutionResult {
  + ExecutionResult(boolean, List~String~) 
  + skipped(String) ExecutionResult
  + executed() boolean
  + logs() List~String~
  + executed(String) ExecutionResult
}
class ExpressionEngine {
<<Interface>>
  + evalNumber(String, Map~String, Object~) double
  + evalBool(String, Map~String, Object~) boolean
}
class ExpressionFunctions {
  - ExpressionFunctions() 
  - clamp(List~Object~) double
  - requireArgs(List~Object~, int, String) void
  - contains(List~Object~) boolean
  - hasItem(List~Object~) boolean
  - hasAnyTag(List~Object~) boolean
  + call(String, List~Object~) Object
}
class FixedRng {
  + FixedRng(double[]) 
  + nextDouble() double
  + reset() void
}
class Interpreter {
  + Interpreter(ExpressionEngine, OpcodeRegistry) 
  + runStep(ExecutionContext, Step) ExecutionResult
  - passesChance(ExecutionContext, Step, Map~String, Object~) boolean
  + run(ExecutionContext, List~Step~) List~ExecutionResult~
  - passesCondition(Step, Map~String, Object~) boolean
  + registry() OpcodeRegistry
  - clamp(double, double, double) double
}
class JsonScriptLoader {
  - JsonScriptLoader() 
  - parseValue(JsonNode) Object
  - parseObject(JsonNode) Map~String, Object~
  - textOrNull(JsonNode, String) String?
  + parseSteps(JsonNode) List~Step~
  - parseStep(JsonNode) Step
  + parseOnCast(String) List~Step~
}
class Lexer {
  ~ Lexer(String) 
  - tryTwoCharOperator() Token?
  - skipWhitespace() void
  - isDigit(char) boolean
  - readIdentifier() Token
  ~ next() Token
  - readNumber() Token
  - readString() Token
  - trySingleCharToken() Token?
  - isIdentPart(char) boolean
  - unescape(char) char
  - hasDigitAt(int) boolean
  - match(String) boolean
  - isIdentStart(char) boolean
}
class NoopHandler {
  - NoopHandler() 
  + execute(ExecutionContext, Step) ExecutionResult
}
class Opcode {
<<enumeration>>
  + Opcode() 
  + values() Opcode[]
  + valueOf(String) Opcode
}
class OpcodeHandler {
<<Interface>>
  + execute(ExecutionContext, Step) ExecutionResult
}
class OpcodeRegistry {
  + OpcodeRegistry() 
  + get(Opcode) OpcodeHandler
  + register(Opcode, OpcodeHandler) OpcodeRegistry
  + has(Opcode) boolean
}
class Parser {
  ~ Parser(Lexer, Map~String, Object~) 
  - parseComparison() Object
  ~ parse() Object
  - parseMultiplicative() Object
  - expect(TokenType) void
  - parseEquality() Object
  - parseArray() List~Object~
  - parseOr() Object
  - parseString() Object
  - parseAnd() Object
  - advance() void
  - isComparison(TokenType) boolean
  - parseUnary() Object
  - parsePrimary() Object
  - parseNumber() Object
  - consume(TokenType) void
  - parseIdentifier() Object
  - parseFunctionCall(String) Object
  - parseAdditive() Object
  - parseGrouped() Object
  - compare(TokenType, double, double) boolean
}
class Rng {
<<Interface>>
  + nextDouble() double
}
class ScriptEngine {
  - ScriptEngine(Interpreter) 
  + run(ExecutionContext, List~Step~) List~ExecutionResult~
  + runJson(ExecutionContext, String) List~ExecutionResult~
  + create(ExpressionEngine, OpcodeRegistry) ScriptEngine
  + registry() OpcodeRegistry
  + createDefault() ScriptEngine
}
class SimpleExecutionContext {
  + SimpleExecutionContext(Rng, Map~String, Object~) 
  + vars() Map~String, Object~
  + clearEvents() void
  + emit(String, Map~String, Object~) void
  + events() List~Event~
  + rng() Rng
}
class SimpleExpressionEngine {
  + SimpleExpressionEngine() 
  + evalNumber(String, Map~String, Object~) double
  - evaluate(String, Map~String, Object~) Object
  + evalBool(String, Map~String, Object~) boolean
}
class Step {
  + Step(Opcode, Map~String, Object~, String, String) 
  + Step(Opcode) 
  + Step(Opcode, Map~String, Object~) 
  + args() Map~String, Object~
  + opcode() Opcode
  + chance() String
  + condition() String
}
class Token {
  ~ Token(TokenType, String) 
  + type() TokenType
  + text() String
}
class TokenType {
<<enumeration>>
  + TokenType() 
  + values() TokenType[]
  + valueOf(String) TokenType
}

Coerce  ..>  Coerce 
DefaultOpcodes  ..>  DefaultOpcodes 
DefaultOpcodes  ..>  EmitHandler 
DefaultOpcodes  ..>  EmitOpcodeHandler : «create»
DefaultOpcodes  ..>  ExecutionResult 
DefaultOpcodes  ..>  NoopHandler 
DefaultOpcodes  ..>  Opcode 
DefaultOpcodes  ..>  Opcode 
DefaultOpcodes  ..>  OpcodeRegistry : «create»
DefaultOpcodes  ..>  OpcodeRegistry 
EmitHandler  ..>  EmitHandler : «create»
EmitHandler "1" *--> "INSTANCE 1" EmitHandler 
EmitHandler  ..>  ExecutionContext 
EmitHandler  ..>  ExecutionResult 
EmitHandler  ..>  OpcodeHandler 
EmitHandler  ..>  Step 
EmitOpcodeHandler  ..>  ExecutionContext 
EmitOpcodeHandler  ..>  ExecutionResult 
EmitOpcodeHandler  ..>  OpcodeHandler 
EmitOpcodeHandler  ..>  Step 
SimpleExecutionContext  -->  Event 
ExecutionContext  ..>  Rng 
ExecutionResult  ..>  ExecutionResult : «create»
ExpressionFunctions  ..>  Coerce 
ExpressionFunctions  ..>  ExpressionFunctions 
FixedRng  ..>  Rng 
Interpreter  ..>  ExecutionContext 
Interpreter  ..>  ExecutionResult 
Interpreter "1" *--> "expressions 1" ExpressionEngine 
Interpreter  ..>  Interpreter 
Interpreter  ..>  OpcodeHandler 
Interpreter "1" *--> "registry 1" OpcodeRegistry 
Interpreter  ..>  Rng 
Interpreter  ..>  Step 
JsonScriptLoader  ..>  JsonScriptLoader 
JsonScriptLoader  ..>  Opcode 
JsonScriptLoader  ..>  Step : «create»
Lexer  ..>  Lexer 
Lexer  ..>  Token : «create»
Lexer  ..>  TokenType 
NoopHandler  ..>  ExecutionContext 
NoopHandler  ..>  ExecutionResult 
NoopHandler  ..>  NoopHandler : «create»
NoopHandler "1" *--> "INSTANCE 1" NoopHandler 
NoopHandler  ..>  OpcodeHandler 
NoopHandler  ..>  Step 
OpcodeHandler  ..>  ExecutionContext 
OpcodeHandler  ..>  ExecutionResult 
OpcodeHandler  ..>  Step 
OpcodeRegistry "1" *--> "handlers *" Opcode 
OpcodeRegistry "1" *--> "handlers *" OpcodeHandler 
OpcodeRegistry  ..>  OpcodeRegistry 
Parser  ..>  Coerce 
Parser  ..>  ExpressionFunctions 
Parser "1" *--> "lexer 1" Lexer 
Parser  ..>  Parser 
Parser "1" *--> "current 1" Token 
Parser  ..>  TokenType 
ScriptEngine  ..>  DefaultOpcodes 
ScriptEngine  ..>  ExecutionContext 
ScriptEngine  ..>  ExecutionResult 
ScriptEngine  ..>  ExpressionEngine 
ScriptEngine "1" *--> "interpreter 1" Interpreter 
ScriptEngine  ..>  Interpreter : «create»
ScriptEngine  ..>  JsonScriptLoader 
ScriptEngine  ..>  OpcodeRegistry 
ScriptEngine  ..>  ScriptEngine : «create»
ScriptEngine  ..>  SimpleExpressionEngine 
ScriptEngine  ..>  Step 
SimpleExecutionContext "1" *--> "events *" Event 
SimpleExecutionContext  ..>  Event : «create»
SimpleExecutionContext  ..>  ExecutionContext 
SimpleExecutionContext "1" *--> "rng 1" Rng 
SimpleExpressionEngine  ..>  Coerce 
SimpleExpressionEngine  ..>  ExpressionEngine 
SimpleExpressionEngine  ..>  Lexer : «create»
SimpleExpressionEngine  ..>  Parser : «create»
SimpleExpressionEngine  ..>  SimpleExpressionEngine : «create»
SimpleExpressionEngine "1" *--> "INSTANCE 1" SimpleExpressionEngine 
Step "1" *--> "opcode 1" Opcode 
Step  ..>  Step 
Token "1" *--> "type 1" TokenType 
