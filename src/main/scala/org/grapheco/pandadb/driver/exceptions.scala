package org.grapheco.pandadb.driver

/**
 * @program: pandadb-java-driver
 * @description:
 * @author: LiamGao
 * @create: 2022-01-18 14:46
 */
class NotImplementMethodException(msg: String) extends Exception{
  override def getMessage: String = s"${msg} Method not implemented..."
}

class UsernameOrPasswordErrorException extends Exception{
  override def getMessage: String = "username or password error..."
}

class NotValidAddressException() extends Exception{
  override def getMessage: String = "not a valid address, please check your uri..."
}

class NotValidSchemaException(msg: String) extends Exception{
  override def getMessage: String = s"uri should start with: ${msg}://"
}

class CypherErrorException(msg: String) extends Exception{
  override def getMessage: String = msg
}

class UnknownErrorException(msg: String) extends Exception{
  override def getMessage: String = s"unknown error when $msg "
}
