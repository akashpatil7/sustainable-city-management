from src.common.response import Response

class EndPointResolver:

    @staticmethod
    def perform_action(instance, action):
        className = instance.__class__.__name__
        try:
            return getattr(instance, action)()
        except KeyError:
            print("[" + className + ", " + action + "] EndPoint not found")
        except AttributeError:
            print("[" + className + ", " + action + "] EndPoint cannot be resolved")
        return Response.not_found_404(className + ": " + action +
                                    " not found")
