package service

import core.DefaultTimeout
import org.specs2._
import org.specs2.specification._


class GodzillaServiceSpec extends Specification { def is = s2"""

  This is a specification of the Godzilla Service.

  We can connect to Spark and retrieve data.

  Retrieve all casts with temperatures great than 13 degrees above average $ok
  """
}
