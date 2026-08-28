#if canImport(Testing)
import Testing
import Clap

@Suite("Clap Swift Export Tests")
struct ClapExportTests {
    @Test("Clap swift module imported cleanly")
    func testSwiftModuleLoads() throws {
        #expect(Bool(true), "Clap swift module imported cleanly")
    }
}
#elseif canImport(XCTest)
import XCTest
import Clap

final class ClapExportTests: XCTestCase {
    func testSwiftModuleLoads() throws {
        XCTAssertTrue(true, "Clap swift module imported cleanly")
    }
}
#endif
